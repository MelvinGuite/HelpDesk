package com.tickets;

import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.mysql.Connmysql;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


/**
 * Servlet implementation class AtencionTicket
 */
@WebServlet("/AtencionTicket")
public class AtencionTicket extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String identificacion = request.getParameter("identificacion");
		ArrayList<String>arrTicket = new ArrayList<String>();
		// System.out.println("llamada a ticket en espera");
		String numero_area = null;
		try {
			Connmysql conn = new Connmysql();
			ResultSet rsArea = conn.Area(Integer.parseInt(identificacion));
			if(rsArea.next()) {
				numero_area = rsArea.getString("id_area");
				ResultSet rsTicket = conn.TicketArea(Integer.parseInt(numero_area));
				while (rsTicket.next()) {
					arrTicket.add(rsTicket.getString("id_ticket"));
					arrTicket.add(rsTicket.getString("estado"));
				}
				
			}
			conn.cerrarConexion();
			Gson gson = new Gson();
			String json = gson.toJson(arrTicket);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			
			response.getWriter().write(json);
			// System.out.println("Mandando respuesta json de ticket");
			// System.out.println(json);
		}catch (Exception e) {
			e.printStackTrace();
			response.getWriter().write("Error en el servidor");
		}
	}
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
