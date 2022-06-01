package com.example.netty.bytebuf;

import io.netty.buffer.*;
import lombok.extern.slf4j.Slf4j;

/**
 * 零拷贝实现
 */
@Slf4j
public class Test01 {
    public static void main(String[] args) {
        ByteBuf a = ByteBufAllocator.DEFAULT.buffer(5);
        a.writeBytes(new byte[]{1, 2, 3, 4, 5});

        ByteBuf b = ByteBufAllocator.DEFAULT.buffer(5);
        b.writeBytes(new byte[]{6, 7, 8, 9, 10});

        //组合a b
        //2次复制
//        ByteBuf c = ByteBufAllocator.DEFAULT.buffer(10);
//        c.writeBytes(a).writeBytes(b);

        //逻辑上
        CompositeByteBuf compositeByteBuf = ByteBufAllocator.DEFAULT.compositeBuffer();
        compositeByteBuf.addComponents(true, a, b);
        log.info("\n{}", ByteBufUtil.prettyHexDump(compositeByteBuf));

        //底层使用CompositeByteBuf
        ByteBuf buf = Unpooled.wrappedBuffer(a, b);
    }
}
