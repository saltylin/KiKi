package cn.edu.nju.pasalab.kiki.client.network;

import cn.edu.nju.pasalab.kiki.client.ClientContext;
import cn.edu.nju.pasalab.kiki.common.Configuration;
import cn.edu.nju.pasalab.kiki.common.Constants;
import cn.edu.nju.pasalab.kiki.common.network.message.CompleteIDStoreRequest;
import cn.edu.nju.pasalab.kiki.common.network.message.CompleteIDStoreResponse;
import cn.edu.nju.pasalab.kiki.common.network.message.CreateIDStoreRequest;
import cn.edu.nju.pasalab.kiki.common.network.message.CreateIDStoreResponse;
import cn.edu.nju.pasalab.kiki.common.network.message.DecodeRequest;
import cn.edu.nju.pasalab.kiki.common.network.message.DecodeResponse;
import cn.edu.nju.pasalab.kiki.common.network.message.DeleteIDStoreRequest;
import cn.edu.nju.pasalab.kiki.common.network.message.DeleteIDStoreResponse;
import cn.edu.nju.pasalab.kiki.common.network.message.EncodeRequest;
import cn.edu.nju.pasalab.kiki.common.network.message.EncodeResponse;
import cn.edu.nju.pasalab.kiki.common.network.message.ErrorMessage;
import cn.edu.nju.pasalab.kiki.common.network.message.IDStoreSizeRequest;
import cn.edu.nju.pasalab.kiki.common.network.message.IDStoreSizeResponse;
import cn.edu.nju.pasalab.kiki.common.network.message.Message;

import com.google.common.base.Preconditions;

import io.netty.channel.Channel;

import javax.annotation.concurrent.ThreadSafe;

import java.io.Closeable;
import java.io.IOException;
import java.util.Arrays;

@ThreadSafe
public final class NetworkProxy implements Closeable {
  private final Configuration conf;
  private volatile Channel channel;
  private volatile boolean initialized = false;
  private final long requestTimeoutMs;

  private final Object signal = new Object();

  private volatile CreateIDStoreResponse createIDStoreResponse;
  private volatile EncodeResponse encodeResponse;
  private volatile DecodeResponse decodeResponse;
  private volatile CompleteIDStoreResponse completeIDStoreResponse;
  private volatile IDStoreSizeResponse idStoreSizeResponse;
  private volatile DeleteIDStoreResponse deleteIDStoreResponse;

  private volatile ErrorMessage errorMessage;

  public NetworkProxy() {
    conf = ClientContext.getConf();
    requestTimeoutMs = conf.getLong(Constants.CLIENT_REQUEST_TIMEOUT_MS);
  }

  public synchronized void initChannel(Channel channel) {
    Preconditions.checkState(!initialized,
        "The channel of the network proxy has already been initialized");
    this.channel = channel;
    initialized = true;
  }

  public synchronized CreateIDStoreResponse createIDStore(CreateIDStoreRequest request)
      throws IOException, InterruptedException {
    checkInitialized();
    synchronized (signal) {
      channel.writeAndFlush(request);
      signal.wait(requestTimeoutMs);
    }
    CreateIDStoreResponse ret = createIDStoreResponse;
    createIDStoreResponse = null;
    if (ret == null || ret.getStoreID() != request.getStoreID()) {
      throw new IOException(String.format("Failed to create ID store %d", request.getStoreID()));
    }
    return ret;
  }

  public synchronized EncodeResponse encode(EncodeRequest request) throws IOException,
      InterruptedException {
    checkInitialized();
    synchronized (signal) {
      channel.writeAndFlush(request);
      signal.wait(requestTimeoutMs);
    }
    EncodeResponse ret = encodeResponse;
    encodeResponse = null;
    if (ret == null || ret.getStoreID() != request.getStoreID()
        || !Arrays.equals(ret.getKey(), request.getKey())) {
      throw new IOException(String.format("Failed to encode %s in ID store %d",
          Arrays.toString(request.getKey()), request.getStoreID()));
    }
    return ret;
  }

  public synchronized DecodeResponse decode(DecodeRequest request) throws IOException,
      InterruptedException {
    checkInitialized();
    synchronized (signal) {
      channel.writeAndFlush(request);
      signal.wait(requestTimeoutMs);
    }
    DecodeResponse ret = decodeResponse;
    decodeResponse = null;
    if (ret == null || ret.getStoreID() != request.getStoreID() || ret.getID() != request.getID()) {
      throw new IOException((String.format("Failed to decode %d in ID store %d", request.getID(),
          request.getStoreID())));
    }
    return ret;
  }

  public synchronized CompleteIDStoreResponse completeIDStore(CompleteIDStoreRequest request)
      throws IOException, InterruptedException {
    checkInitialized();
    synchronized (signal) {
      channel.writeAndFlush(request);
      signal.wait(requestTimeoutMs);
    }
    CompleteIDStoreResponse ret = completeIDStoreResponse;
    completeIDStoreResponse = null;
    if (ret == null || request.getStoreID() != ret.getStoreID()) {
      throw new IOException(String.format("Failed to complete ID store %d", request.getStoreID()));
    }
    return ret;
  }

  public synchronized IDStoreSizeResponse getIDStoreSize(IDStoreSizeRequest request)
      throws IOException, InterruptedException {
    checkInitialized();
    synchronized (signal) {
      channel.writeAndFlush(request);
      signal.wait(requestTimeoutMs);
    }
    IDStoreSizeResponse ret = idStoreSizeResponse;
    idStoreSizeResponse = null;
    if (ret == null || request.getStoreID() != ret.getStoreID()) {
      throw new IOException(
          String.format("Failed to get the size of ID store %d", request.getStoreID()));
    }
    return ret;
  }

  public synchronized DeleteIDStoreResponse deleteIDStore(DeleteIDStoreRequest request)
      throws IOException, InterruptedException {
    checkInitialized();
    synchronized (signal) {
      channel.writeAndFlush(request);
      signal.wait(requestTimeoutMs);
    }
    DeleteIDStoreResponse ret = deleteIDStoreResponse;
    deleteIDStoreResponse = null;
    if (ret == null || request.getStoreID() != ret.getStoreID()) {
      throw new IOException(String.format("Failed to delete ID store %d", request.getStoreID()));
    }
    return ret;
  }

  public void signalResponse(Message msg) {
    checkInitialized();
    switch (msg.getType()) {
      case CREATE_ID_STORE_RESPONSE:
        createIDStoreResponse = (CreateIDStoreResponse) msg;
        break;
      case ENCODE_RESPONSE:
        encodeResponse = (EncodeResponse) msg;
        break;
      case DECODE_RESPONSE:
        decodeResponse = (DecodeResponse) msg;
        break;
      case COMPLETE_ID_STORE_RESPONSE:
        completeIDStoreResponse = (CompleteIDStoreResponse) msg;
        break;
      case ID_STORE_SIZE_RESPONSE:
        idStoreSizeResponse = (IDStoreSizeResponse) msg;
        break;
      case DELETE_ID_STORE_RESPONSE:
        deleteIDStoreResponse = (DeleteIDStoreResponse) msg;
        break;
      default:
        throw new IllegalArgumentException(String.format("Illegal received response type %s",
            msg.getType()));
    }
    synchronized (signal) {
      signal.notify();
    }
  }

  @Override
  public void close() {
    channel.close();
  }

  private void checkInitialized() {
    Preconditions.checkState(initialized,
        "The channel of the network proxy hasn't been initialized");
  }
}
