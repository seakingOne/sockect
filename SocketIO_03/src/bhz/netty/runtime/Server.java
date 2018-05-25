package bhz.netty.runtime;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;

public class Server {

	public static void main(String[] args) throws Exception{
		//创建2个线程组，一个用户服务端处理客户端请求，一个进行网络读写
		EventLoopGroup pGroup = new NioEventLoopGroup();
		EventLoopGroup cGroup = new NioEventLoopGroup();
		//辅助工具类，用于服务器通道的一系列配置
		ServerBootstrap b = new ServerBootstrap();
		b.group(pGroup, cGroup)
		 .channel(NioServerSocketChannel.class) //指定nio模式
		 .option(ChannelOption.SO_BACKLOG, 1024)//设置tcp的缓冲区
		 //设置日志
		 .handler(new LoggingHandler(LogLevel.INFO))
		 .childHandler(new ChannelInitializer<SocketChannel>() {//SocketChannel 获取连接通道
			protected void initChannel(SocketChannel sc) throws Exception {
				sc.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingDecoder());
				sc.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingEncoder());
				sc.pipeline().addLast(new ReadTimeoutHandler(5)); 
				sc.pipeline().addLast(new ServerHandler());//ServerHandler实现业务逻辑
			}
		});
		
		ChannelFuture cf = b.bind("127.0.0.1",8765).sync();//返回连接通道
		
		cf.channel().closeFuture().sync();
		pGroup.shutdownGracefully();
		cGroup.shutdownGracefully();
		
	}
}
