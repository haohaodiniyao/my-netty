package com.example.nio;

/**
 * FileChannel只能工作在阻塞模式下
 * 不能直接打开FileChannel，必须通过FileInputStream、FileOutputStream、RandomAccessFile获取FileChannel
 * int readBytes = channel.read(buffer)
 * -1 到了文件末尾
 * 写入：buffer写入channel SocketChannel
 * ByteBuffer buffer =
 * buffer.put()   //存入数据
 * buffer.flip()  //切换读模式
 * while(buffer.hasRemaining()){
 *     channel.write(buffer);
 * }
 */
public class FileChannel文件编程 {
}
