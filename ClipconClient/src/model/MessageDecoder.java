package model;


import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

import org.json.JSONObject;
/**
 * �������� ���� string�� object(Message)�� decoding. */
public class MessageDecoder implements Decoder.Text<Message> {
	JSONObject tmp;

	public void destroy() {
	}

	public void init(EndpointConfig arg0) {
		tmp = new JSONObject();
	}
	
	public Message decode(String incommingMessage) throws DecodeException {
		System.out.println("�������� ���� �޽���: " + incommingMessage);
		Message message = new Message().setJson(incommingMessage);
		System.out.println(message.getType());
		return message;
	}

	public boolean willDecode(String message) {
		boolean flag = true;
		try {
		} catch (Exception e) {
			flag = false;
		}
		return flag;
	}
}