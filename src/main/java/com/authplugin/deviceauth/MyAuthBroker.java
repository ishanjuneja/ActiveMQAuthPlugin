package com.authplugin.deviceauth;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.activemq.broker.Broker;
import org.apache.activemq.broker.BrokerFilter;
import org.apache.activemq.broker.ConnectionContext;
import org.apache.activemq.command.ConnectionInfo;

public class MyAuthBroker extends BrokerFilter {

	PreparedStatement pstmt;

	public MyAuthBroker(Broker next,Connection con) {
		super(next);
		try {
			pstmt = con.prepareStatement("select * from devices where clientID= ?");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	public void addConnection(ConnectionContext context, ConnectionInfo info) throws Exception {
		try {
			
			pstmt.setString(1, info.getClientId());
			ResultSet rs = pstmt.executeQuery();
		
			if (rs.next()) {
				String clientID = rs.getString("clientID");
				String username = rs.getString("username");
				String password = rs.getString("password");
				if (username.equals(info.getUserName()) && password.equals(info.getPassword())) {
					super.addConnection(context, info);
				} else {
					// username or password does not match
					throw new SecurityException("invalid username or password from deviceID : " + clientID);
				}
			} else {
				// device id not in DB, throw exception
				throw new SecurityException("Client ID not in DB : " + info.getClientId());
			}
		} catch (SecurityException e) {
			System.out.println(e.getMessage());
			throw new Exception();
		}
	}

}
