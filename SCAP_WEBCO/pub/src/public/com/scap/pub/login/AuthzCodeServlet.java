package com.scap.pub.login;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class AuthzCodeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    
    public static final String ATTR_AUTHZ_CODE = "verifyCode";
    
    private static Font[] constant_fonts = {
    		new Font("Ravie", Font.PLAIN, 24),
    		new Font("Antique Olive Compact", Font.PLAIN, 24),
    		new Font("Forte", Font.PLAIN, 24), 
    		new Font("Wide Latin", Font.PLAIN, 24),
    		new Font("Gill Sans Ultra Bold", Font.PLAIN, 24)
    };
    
    private static char[] constant_chars = "abcdefghijklmnopqrstuvwxyz1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("image/png");
		response.setHeader("Cache-Control", "No-cache");
		response.setHeader("Pragma", "No-cache");
		response.setDateHeader("Expires", 0);

		Random random = new Random();
		
		// 画布
		BufferedImage image = new BufferedImage(120, 30, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = image.createGraphics();
		
		// 背景
		g.setColor(new Color(128 + random.nextInt(128),128 + random.nextInt(128),128 + random.nextInt(128)));
		g.fillRect(0, 0, image.getWidth(), image.getHeight());
		
		// 验证码
		char[] codes = randomChars(4);
		for (int i = 0; i < codes.length; i++) {
	        char c = codes[i];
	        Font font = randomFont();
	        Color color = randomColor();
	        
	        g.setColor(color);
	        g.setFont(font);
	        g.drawString(Character.toString(c), i * 30 + random.nextInt(20), font.getSize());
        }
		
		// 混淆点
		for (int i = 0; i< 30; i++) {
			g.setColor(randomColor());
			int x = random.nextInt(image.getWidth());
			int y = random.nextInt(image.getHeight());
			if (i % 2 == 0) {
				g.drawLine(x, y, x + random.nextInt(20) - 10, y + random.nextInt(20) - 10);
			} else {
				int r = random.nextInt(10) + 5;
				g.drawArc(x, y, r, r, 0, random.nextInt(360));
			}
		}
		
		request.getSession().setAttribute(ATTR_AUTHZ_CODE, new String(codes));
		ImageIO.write(image, "PNG", response.getOutputStream());
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    doGet(request, response);
	}
	
	public Font randomFont() {
		Random random = new Random();
		return constant_fonts[random.nextInt(constant_fonts.length)];
	}
	
	public Color randomColor() {
		Random random = new Random();
		return new Color(
			random.nextInt(128),
			random.nextInt(128),
			random.nextInt(128)
		);
	}
	
	public char[] randomChars(int count) {
		char[] str = new char[count];
		Random random = new Random();
		for (int i = 0; i < str.length; i++) {
	        str[i] = constant_chars[random.nextInt(constant_chars.length)];
        }
		return str;
	}
	
}
