package com.authplugin.deviceauth;

import java.sql.Connection;
import java.sql.DriverManager;

import org.apache.activemq.broker.Broker;
import org.apache.activemq.broker.BrokerPlugin;

public class MyAuthPlugin implements BrokerPlugin {

	public String url;
	public String username;
	public String password;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Broker installPlugin(Broker broker) throws Exception {
		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection con = DriverManager.getConnection(this.url, this.username, this.password);
		return new MyAuthBroker(broker, con);
	}

}
