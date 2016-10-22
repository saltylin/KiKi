package cn.edu.nju.pasalab.kiki.server;

import cn.edu.nju.pasalab.kiki.common.Configuration;
import cn.edu.nju.pasalab.kiki.common.Constants;
import cn.edu.nju.pasalab.kiki.common.network.message.CompleteIDStoreRequest;
import cn.edu.nju.pasalab.kiki.common.network.message.CompleteIDStoreResponse;
import cn.edu.nju.pasalab.kiki.common.network.message.CreateIDStoreRequest;
import cn.edu.nju.pasalab.kiki.common.network.message.CreateIDStoreResponse;
import cn.edu.nju.pasalab.kiki.common.network.message.DecodeRequest;
import cn.edu.nju.pasalab.kiki.common.network.message.DeleteIDStoreRequest;
import cn.edu.nju.pasalab.kiki.common.network.message.DeleteIDStoreResponse;
import cn.edu.nju.pasalab.kiki.common.network.message.EncodeRequest;
import cn.edu.nju.pasalab.kiki.common.network.message.ErrorMessage;
import cn.edu.nju.pasalab.kiki.common.network.message.IDStoreSizeRequest;
import cn.edu.nju.pasalab.kiki.common.network.message.IDStoreSizeResponse;
import cn.edu.nju.pasalab.kiki.common.network.message.Message;

import com.google.common.base.Preconditions;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ChannelHandler.Sharable
public final class KikiServerChannelHandler extends ChannelInboundHandlerAdapter {
  private static final Logger LOG = LoggerFactory.getLogger(Constants.LOGGER_TYPE);
  /** The {@link ClientRequestHandler} which handles the requests from the clients. */
  private final ClientRequestHandler clientRequestHandler;
  /** The server configuration. */
  private final Configuration conf = ServerContext.getConf();

  public KikiServerChannelHandler(ClientRequestHandler clientRequestHandler) {
    this.clientRequestHandler = clientRequestHandler;
  }

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) {
    Preconditions.checkArgument(msg instanceof Message, "Illegal message");
    Message.Type type = ((Message) msg).getType();
    switch (type) {
      case CREATE_ID_STORE_REQUEST:
        handleCreateStoreRequest(ctx, (CreateIDStoreRequest) msg);
        break;
      case ENCODE_REQUEST:
        handleEncodeRequest(ctx, (EncodeRequest) msg);
        break;
      case DECODE_REQUEST:
        handleDecodeRequest(ctx, (DecodeRequest) msg);
        break;
      case COMPLETE_ID_STORE_REQUEST:
        handleCompleteIDStoreRequest(ctx, (CompleteIDStoreRequest) msg);
        break;
      case ID_STORE_SIZE_REQUEST:
        handleStoreSizeRequest(ctx, (IDStoreSizeRequest) msg);
        break;
      case DELETE_ID_STORE_REQUEST:
        handleDeleteStoreRequest(ctx, (DeleteIDStoreRequest) msg);
        break;
      default:
        throw new IllegalArgumentException(String.format("Illegal received message type %s", type));
    }
  }

  private void handleCreateStoreRequest(ChannelHandlerContext ctx, CreateIDStoreRequest request) {
    int storeID = request.getStoreID();
    try {
      clientRequestHandler.createStore(storeID);
      ctx.writeAndFlush(new CreateIDStoreResponse(storeID));
    } catch (Exception e) {
      ctx.writeAndFlush(new ErrorMessage(e.getMessage()));
    }
  }

  private void handleEncodeRequest(ChannelHandlerContext ctx, EncodeRequest request) {
    int storeID = request.getStoreID();
    byte[] key = request.getKey();
    try {
      clientRequestHandler.encode(storeID, key, ctx.channel());
    } catch (Exception e) {
      ctx.writeAndFlush(new ErrorMessage(e.getMessage()));
    }
  }

  private void handleDecodeRequest(ChannelHandlerContext ctx, DecodeRequest request) {
    int storeID = request.getStoreID();
    long ID = request.getID();
    try {
      clientRequestHandler.decode(storeID, ID, ctx.channel());
    } catch (Exception e) {
      ctx.writeAndFlush(new ErrorMessage(e.getMessage()));
    }
  }

  private void handleCompleteIDStoreRequest(ChannelHandlerContext ctx,
      CompleteIDStoreRequest request) {
    int storeID = request.getStoreID();
    try {
      clientRequestHandler.completeStore(storeID);
      ctx.writeAndFlush(new CompleteIDStoreResponse(storeID));
    } catch (Exception e) {
      ctx.writeAndFlush(new ErrorMessage(e.getMessage()));
    }
  }

  private void handleDeleteStoreRequest(ChannelHandlerContext ctx, DeleteIDStoreRequest request) {
    int storeID = request.getStoreID();
    try {
      clientRequestHandler.deleteStore(storeID);
      ctx.writeAndFlush(new DeleteIDStoreResponse(storeID));
    } catch (Exception e) {
      ctx.writeAndFlush(new ErrorMessage(e.getMessage()));
    }
  }

  private void handleStoreSizeRequest(ChannelHandlerContext ctx, IDStoreSizeRequest request) {
    int storeID = request.getStoreID();
    try {
      long size = clientRequestHandler.getStoreSize(storeID);
      ctx.writeAndFlush(new IDStoreSizeResponse(storeID, size));
    } catch (Exception e) {
      ctx.writeAndFlush(new ErrorMessage(e.getMessage()));
    }
  }
}
