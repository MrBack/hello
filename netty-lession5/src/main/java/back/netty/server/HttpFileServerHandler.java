package back.netty.server;

import back.utils.ActionBean;
import back.utils.BeanFactory;
import back.utils.JsonUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;

import java.lang.reflect.Method;

public class HttpFileServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    @Override
    protected void messageReceived(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) throws Exception {

        //1、uri
        String uri = fullHttpRequest.uri();
        System.out.println("uri : " +uri);
        ByteBuf byteBuf = fullHttpRequest.content();
        String params = byteBuf.toString();
        System.out.println("params : " + params);

        HttpMethod method = fullHttpRequest.method();
        System.out.println("http method : " + method.name());

        HttpVersion httpVersion = fullHttpRequest.protocolVersion();
        System.out.println("httpVersion : " + httpVersion);

        HttpHeaders headers = fullHttpRequest.headers();
        System.out.println("headers : ");
        headers.forEach(entry -> {
            System.out.println("key :" + entry.getKey() + ", value : " + entry.getValue());
        });
        Object resp = null;
        BeanFactory.start();
        ActionBean actionBean = BeanFactory.map.get(method.toString()).get(uri);
        if(actionBean != null){
            Method method1 = actionBean.getMethod();
            Object bean = actionBean.getBean();
            resp = method1.invoke(bean);
        }
        response(channelHandlerContext, resp);
    }

    private void response(ChannelHandlerContext ctx, Object resp){

        String parse = JsonUtils.parse(resp);
        System.out.println("Response : " + parse);
        DefaultFullHttpResponse defaultFullHttpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, Unpooled.copiedBuffer(parse.getBytes()));
        defaultFullHttpResponse.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html; charset=UTF-8");
        ctx.writeAndFlush(defaultFullHttpResponse).addListener(ChannelFutureListener.CLOSE);
        System.out.println("===========================================================================");
    }
}
