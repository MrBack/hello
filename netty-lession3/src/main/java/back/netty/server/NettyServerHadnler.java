package back.netty.server;


import back.config.Constants;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.util.concurrent.atomic.AtomicInteger;

public class NettyServerHadnler extends ChannelHandlerAdapter {

    private AtomicInteger count = new AtomicInteger(0);
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        int num = count.getAndIncrement();
        String requestMsg = (String)msg;
        System.out.println("本次请求内容：" + requestMsg + "请求次数：" + num + " , 当前时间：" + System.currentTimeMillis());
        String responseMsg = "Resp_" + requestMsg + Constants.DELEMITED_SEPARATOR;
        ByteBuf byteBuf = Unpooled.copiedBuffer(responseMsg.getBytes());
        ctx.writeAndFlush(byteBuf);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
