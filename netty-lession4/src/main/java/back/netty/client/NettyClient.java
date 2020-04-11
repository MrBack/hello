package back.netty.client;

import back.config.ConfigUtils;
import back.config.Constants;
import com.typesafe.config.Config;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

public class NettyClient {

    private NettyClient(){};
    private static NettyClient nettyClient = new NettyClient();
    public static NettyClient getInstance(){
        return nettyClient;
    }

    private volatile static EventLoopGroup group = null;
    private volatile static Bootstrap bootstrap = null;

    public void start(){

        Config config = ConfigUtils.getConfig();
        String host = config.getString("server.host");
        int port = config.getInt("server.port");
        if(group == null){
            synchronized (NettyClient.class){
                if(group == null){
                    group = new NioEventLoopGroup();
                }
            }
        }

        if(bootstrap == null){
            synchronized (NettyClient.class){
                if(bootstrap == null){
                   try{
                       bootstrap = new Bootstrap();
                       bootstrap.group(group).channel(NioSocketChannel.class)
                               .option(ChannelOption.TCP_NODELAY, true)
                               .handler(new ChannelInitializer<SocketChannel>() {
                                   @Override
                                   protected void initChannel(SocketChannel socketChannel) throws Exception {
                                       socketChannel.pipeline().addLast(new FixedLengthFrameDecoder(11));
                                       socketChannel.pipeline().addLast(new StringDecoder());
                                       socketChannel.pipeline().addLast(new NettyClientHandler());
                                   }
                               });
                       ChannelFuture future = bootstrap.connect(host, port).sync();
                       future.channel().closeFuture().sync();
                   }catch (Exception e){
                       System.out.println("异常信息：" + e.getStackTrace());
                   }finally {
                        close();
                   }
                }
            }
        }
    }

    public void close(){
        if(group != null){
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        NettyClient.getInstance().start();
    }
}
