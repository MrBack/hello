package com.back.netty.server;

import com.back.config.ConfigUtils;
import com.typesafe.config.Config;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * Lession1:
 */
public class NettyServer {

    private NettyServer(){};
    private static NettyServer nettyServer = new NettyServer();
    public static NettyServer getInstance(){
        return nettyServer;
    }
    private volatile static EventLoopGroup workerGroup = null;
    private volatile static EventLoopGroup bossGroup = null;
    private volatile static ServerBootstrap serverBootstrap = null;

    public void start(){
        Config config = ConfigUtils.getConfig();
        int port = config.getInt("server.port");

        if(workerGroup == null){
            synchronized (NettyServer.class){
                if(workerGroup == null){
                    workerGroup = new NioEventLoopGroup();
                }
            }
        }

        if(bossGroup == null){
            synchronized (NettyServer.class){
                if(bossGroup == null){
                    bossGroup = new NioEventLoopGroup();
                }
            }
        }

        if(serverBootstrap == null){
            synchronized (NettyServer.class){
                if(serverBootstrap == null){
                    try{
                        serverBootstrap = new ServerBootstrap();
                        serverBootstrap.group(bossGroup, workerGroup)
                                .channel(NioServerSocketChannel.class)
                                .option(ChannelOption.SO_BACKLOG, 1024)
                                .childHandler(new ChildChannelHandler());
                        ChannelFuture future = serverBootstrap.bind(port).sync();
                        System.out.println("Netty服务开启 ！ 端口：" + port);
                        future.channel().closeFuture().sync();
                    }catch (Exception e){
                        System.out.println("异常信息：" + e.getStackTrace());
                    }finally {
                        this.close();
                    }
                }
            }
        }
    }

    private void close(){
        if(bossGroup != null){
            bossGroup.shutdownGracefully();
        }
        if(workerGroup != null){
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) {

        NettyServer.getInstance().start();
    }
}
