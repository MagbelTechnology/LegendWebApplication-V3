package audit;

interface ISMInterface
 {
 final short ALERT = 1;
 final short ERROR = 2;
 final short SYSTEM_ERROR = 4;
 final short DB_CONNECT_ERROR = 8;
 final short RES_SHARE_ERROR = 16;
 final short RES_AVAIL_ERROR = 32;
}