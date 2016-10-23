package cn.edu.nju.pasalab.kiki.common.network.message;

import cn.edu.nju.pasalab.kiki.common.network.Encodable;
import io.netty.buffer.ByteBuf;

public interface Message extends Encodable {
  Type getType();

  enum Type implements Encodable {
    CREATE_ID_STORE_REQUEST(0),
    CREATE_ID_STORE_RESPONSE(1),
    ENCODE_REQUEST(2),
    ENCODE_RESPONSE(3),
    DECODE_REQUEST(4),
    DECODE_RESPONSE(5),
    COMPLETE_ID_STORE_REQUEST(6),
    COMPLETE_ID_STORE_RESPONSE(7),
    DELETE_ID_STORE_REQUEST(8),
    DELETE_ID_STORE_RESPONSE(9),
    ID_STORE_SIZE_REQUEST(10),
    ID_STORE_SIZE_RESPONSE(11),
    ERROR_MESSAGE(12);

    private final byte id;

    Type(int id) {
      this.id = (byte) id;
    }

    public byte getID() {
      return id;
    }

    public void encode(ByteBuf out) {
      out.writeByte(id);
    }

    public int getEncodedLength() {
      return 1;
    }

    public static Type decode(ByteBuf in) {
      byte id = in.readByte();
      switch (id) {
        case 0:
          return CREATE_ID_STORE_REQUEST;
        case 1:
          return CREATE_ID_STORE_RESPONSE;
        case 2:
          return ENCODE_REQUEST;
        case 3:
          return ENCODE_RESPONSE;
        case 4:
          return DECODE_REQUEST;
        case 5:
          return DECODE_RESPONSE;
        case 6:
          return COMPLETE_ID_STORE_REQUEST;
        case 7:
          return COMPLETE_ID_STORE_RESPONSE;
        case 8:
          return DELETE_ID_STORE_REQUEST;
        case 9:
          return DELETE_ID_STORE_RESPONSE;
        case 10:
          return ID_STORE_SIZE_REQUEST;
        case 11:
          return ID_STORE_SIZE_RESPONSE;
        case 12:
          return ERROR_MESSAGE;
        default:
          throw new IllegalArgumentException(String.format("No corresponding type id for %d", id));
      }
    }
  }
}
