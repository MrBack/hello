package back.netty.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 *
 *
 */
public class ChildChannelHandler extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        socketChannel.pipeline().addLast("http-decoder", new HttpRequestDecoder());
        socketChannel.pipeline().addLast("http-aggregator", new HttpObjectAggregator(65536));
        socketChannel.pipeline().addLast("http-encoder", new HttpResponseEncoder());
        socketChannel.pipeline().addLast("http-chunked", new ChunkedWriteHandler());
        socketChannel.pipeline().addLast("fileServerHandler", new HttpFileServerHandler());
    }
}
