package back.netty.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.util.concurrent.atomic.AtomicInteger;

public class NettyClientHandler extends ChannelHandlerAdapter {

    private byte[] req;
    private static AtomicInteger count = new AtomicInteger(0);
    public NettyClientHandler(){
        req = ("Request_msg" + System.getProperty("line.separator")).getBytes();
    }

    /**
     * 模拟请求多次
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ByteBuf buff = null;
        for (int i = 0; i < 100; i++) {
            buff = Unpooled.buffer(req.length);
            buff.writeBytes(req);
            System.out.println("Client准备的请求内容：" + new String(req) + " , 请求次数：" + count.getAndIncrement());
            ctx.writeAndFlush(buff);
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        byte[] bytes = new byte[buf.readableBytes()];
        buf.readBytes(bytes);
        System.out.println("Client响应报文：" + new String(bytes) + System.lineSeparator());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
