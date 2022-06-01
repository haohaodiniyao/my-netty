package com.example.netty.slice;

import com.example.nio.util.Show;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import lombok.extern.slf4j.Slf4j;

/**
 * 切片
 * slice
 * 数据不复置
 * <p>
 * 引用计数算法
 */
@Slf4j
public class SliceTest {
    public static void main(String[] args) {
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer(10);
        buffer.writeBytes(new byte[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j'});
        //最大容量限制了
        ByteBuf a = buffer.slice(0, 5);
        a.retain();//+1
        ByteBuf b = buffer.slice(5, 5);
        Show.log(buffer);
        Show.log(a);
        Show.log(b);

        log.info("释放 {}", buffer);
        buffer.release();//-1
        Show.log(a);
        a.release();
        b.release();
    }
}
