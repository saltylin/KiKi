package cn.edu.nju.pasalab.kiki.common.network.message;

import io.netty.buffer.ByteBuf;

public abstract class AbstractMessage implements Message {
  public static Message decode(ByteBuf in) {
    Type type = Type.decode(in);
    switch (type) {
      case CREATE_ID_STORE_REQUEST:
        return CreateIDStoreRequest.decode(in);
      case CREATE_ID_STORE_RESPONSE:
        return CreateIDStoreResponse.decode(in);
      case ENCODE_REQUEST:
        return EncodeRequest.decode(in);
      case ENCODE_RESPONSE:
        return EncodeResponse.decode(in);
      case DECODE_REQUEST:
        return DecodeRequest.decode(in);
      case DECODE_RESPONSE:
        return DecodeResponse.decode(in);
      case CLOSE_ID_STORE_REQUEST:
        return CloseIDStoreRequest.decode(in);
      case CLOSE_ID_STORE_RESPONSE:
        return CloseIDStoreResponse.decode(in);
      case DELETE_ID_STORE_REQUEST:
        return DeleteIDStoreRequest.decode(in);
      case DELETE_ID_STORE_RESPONSE:
        return DeleteIDStoreResponse.decode(in);
      case ID_STORE_SIZE_REQUEST:
        return IDStoreSizeRequest.decode(in);
      case ID_STORE_SIZE_RESPONSE:
        return IDStoreSizeResponse.decode(in);
      case ERROR_MESSAGE:
        return ErrorMessage.decode(in);
      default:
        throw new IllegalArgumentException(
            String.format("No corresponding message for type %s", type));
    }
  }
}
