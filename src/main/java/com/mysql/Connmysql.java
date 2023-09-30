package com.mysql;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Connmysql {
	private String url = "jdbc:mysql://database-1.clrghkrtdl1q.us-east-2.rds.amazonaws.com:3306/prueba";
	private String usuario = "admin";
	private String password = "1829372MG";
	private Connection conexion = null;

	// ___________________________Conexion a la base de
	// datos_____________________________________//
	public Connmysql() throws SQLException {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conexion = DriverManager.getConnection(url, usuario, password);
			// System.out.println("Conexión establecida");
		} catch (ClassNotFoundException e) {
			System.out.println("Error al cargar el driver");
			e.printStackTrace();
		} catch (SQLException e) {
			System.out.println("Error en la conexión");
			e.printStackTrace();
		}
	}

	public void cerrarConexion() {
		try {
			conexion.close();
			// System.out.println("Conexión cerrada");
		} catch (SQLException e) {
			System.out.println("Error al cerrar la conexión");
			e.printStackTrace();
		}
	}

	// Registra area
	public void RegistraArea(String nombre_area) throws SQLException {
		CallableStatement cs = conexion.prepareCall(" {call RegistraArea (?) }  ");
		System.out.println("Area Registrada");
		cs.setString(1, nombre_area);
		cs.execute();
		cs.close();
	}

	// consulta areas
	public ResultSet VerArea() throws SQLException {
		Statement st = conexion.createStatement();
		String consulta = "Select * from area ; ";
		return st.executeQuery(consulta);
	}

	// Registra rol
	public void RegistraRol(String nombre_rol) throws SQLException {
		CallableStatement cs = conexion.prepareCall(" {call RegistraRol (?) }");
		System.out.println("Rol Registrado");
		cs.setString(1, nombre_rol);
		cs.execute();
		cs.close();
	}

	// consulta roles
	public ResultSet VerRol() throws SQLException {
		Statement st = conexion.createStatement();
		String consulta = "Select * from rol ; ";
		return st.executeQuery(consulta);
	}

	// Registra empleado

	public void RegistraEmpleado(ArrayList<String> Datos) throws SQLException {
		CallableStatement cs = conexion.prepareCall(" { call RegistraEmpleado ( ?, ?, ?, ?, ? ) }");
		cs.setInt(1, Integer.parseInt(Datos.get(0)));
		cs.setString(2, Datos.get(1));
		cs.setString(3, Datos.get(2));
		cs.setInt(4, Integer.parseInt(Datos.get(3)));
		cs.setInt(5, Integer.parseInt(Datos.get(4)));
		cs.execute();
		System.out.println("Empleado Registrado");
	}

	// Registro Colegiado

	public void RegistraColegiado(ArrayList<String> datos) throws SQLException {
		CallableStatement cs = conexion.prepareCall(" {call RegistraColegiado (?, ?, ?, ?, ?, ?) } ");
		cs.setInt(1, Integer.parseInt(datos.get(0)));
		cs.setString(2, datos.get(1));
		cs.setString(3, datos.get(2));
		cs.setInt(4, Integer.parseInt(datos.get(3)));
		cs.setString(5, datos.get(4));
		cs.setInt(6, Integer.parseInt(datos.get(5)));
		cs.execute();
		System.out.println("Colegiado Registrado");
	}

	// Verifica colegiado
	public ResultSet nombre_colegiado(int colegiado) throws SQLException {
		String consulta = "select nombre, apellido from usuario where id_colegiado  = ? ;";
		PreparedStatement ps = conexion.prepareStatement(consulta);
		ps.setInt(1, colegiado);
		return ps.executeQuery();
	}

	// Consulta Ticket en espera
	public ResultSet Ticket_espera() throws SQLException {
		Statement st = conexion.createStatement();
		String consulta = "SELECT\r\n" + "    t.id_ticket,\r\n" + "    a.nombre AS area,\r\n" + "    CONCAT(\r\n"
				+ "        HOUR(t.fecha_creacion),\r\n" + "        ':',\r\n" + "        MINUTE(t.fecha_creacion),\r\n"
				+ "        ':',\r\n" + "        SECOND(t.fecha_creacion)\r\n" + "    ) AS hora_min_seg\r\n" + "FROM\r\n"
				+ "    ticket t\r\n" + "JOIN\r\n" + "    estado_ticket et ON t.estado_ticket = et.id_estado_ticket\r\n"
				+ "JOIN\r\n" + "    area a ON t.id_area = a.id_area\r\n" + "WHERE\r\n" + "    et.nombre = 'Espera'; ";
		return st.executeQuery(consulta);
	}

	// consulta ticket llamada
	public ResultSet Ticket_llamada() throws SQLException {
		Statement st = conexion.createStatement();
		String consulta = "SELECT t.id_ticket, a.nombre AS area\n" + "FROM ticket t\n"
				+ "JOIN estado_ticket et ON t.estado_ticket = et.id_estado_ticket\n"
				+ "JOIN area a ON t.id_area = a.id_area\n" + "WHERE et.nombre = 'Llamada'; ";
		return st.executeQuery(consulta);
	}

	// consulta ticket atendiendo
	public ResultSet Ticket_atendiendo() throws SQLException {
		Statement st = conexion.createStatement();
		String consulta = "SELECT t.id_ticket, a.nombre AS area, t.fecha_actualizacion\n" + "FROM ticket t\n"
				+ "JOIN estado_ticket et ON t.estado_ticket = et.id_estado_ticket\n"
				+ "JOIN area a ON t.id_area = a.id_area\n" + "WHERE et.nombre = 'Atendiendo'; ";
		return st.executeQuery(consulta);
	}

	// consulta ticket Finalizado
	public ResultSet Ticket_finalizado() throws SQLException {
		Statement st = conexion.createStatement();
		String consulta = "SELECT t.id_ticket, a.nombre AS area, t.fecha_cierre\n" + "FROM ticket t\n"
				+ "JOIN estado_ticket et ON t.estado_ticket = et.id_estado_ticket\n"
				+ "JOIN area a ON t.id_area = a.id_area\n" + "WHERE et.nombre = 'Finalizado'; ";
		return st.executeQuery(consulta);
	}

	// mostrar solo tickets asiognados a un area
	public ResultSet ticket_Area(int identificacion) throws SQLException {
		String consulta = "select \r\n" + "t.id_ticket,\r\n" + "a.nombre, a.id_area, \r\n"
				+ "e.dpi_empleado, e.nombre as empleado, e.apellido,\r\n" + "et.nombre as estado\r\n" + "from \r\n"
				+ "ticket t join area a \r\n" + "on t.id_area = a.id_area\r\n"
				+ "left join empleado e on a.id_area = e.id_area join estado_ticket et	\r\n"
				+ "on et.id_estado_ticket = t.estado_ticket\r\n" + "where t.estado_ticket in( 1, 2, 3) and exists (\r\n"
				+ "select 1 from \r\n" + "empleado e where e.id_area = a.id_area and e.dpi_empleado = ?\r\n" + ");";
		PreparedStatement ps = conexion.prepareStatement(consulta);
		ps.setInt(1, identificacion);
		return ps.executeQuery();
	}

	// Retorna la pagina a la que accede equipo de sopoerte
	public ResultSet login(int dpi, String correo) throws SQLException {
		String consulta1 = "select 1 from empleado where correo = ? and dpi_empleado = ?;";
		String consulta2 = " SELECT DISTINCT a.pagina\r\n" + "FROM empleado AS e\r\n"
				+ "JOIN rol AS r ON e.id_rol = r.id_rol\r\n" + "JOIN accesos AS a ON r.id_rol = a.id_rol\r\n"
				+ "WHERE e.dpi_empleado = ?;";
		PreparedStatement ps = conexion.prepareStatement(consulta1);
		ps.setString(1, correo);
		ps.setInt(2, dpi);
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			PreparedStatement st = conexion.prepareStatement(consulta2);
			st.setInt(1, dpi);
			return st.executeQuery();
		} else {
			return null;
		}
	}

	// Al hacer el login comprueba que exista el usuario
	public boolean Comprueba(int dpi, String correo) throws SQLException {
		String consulta1 = "select 1 from empleado where correo = ? and dpi_empleado = ?;";
		PreparedStatement ps = conexion.prepareStatement(consulta1);
		ps.setString(1, correo);
		ps.setInt(2, dpi);
		ResultSet rs = ps.executeQuery();

		if (rs.next()) {
			return true;
		} else {
			return false;
		}
	}

	public void llama_ticket(int ticket) throws SQLException {
		CallableStatement sc = conexion.prepareCall(" { call llamar_ticket (?) }");
		sc.setInt(1, ticket);
		sc.execute();
	}

	public void Retorna_ticket(int ticket) throws SQLException {
		CallableStatement cl = conexion.prepareCall(" { call Retorna_Ticket (?) } ");
		cl.setInt(1, ticket);
		cl.execute();
	}

	
	public void Atentiendo(int ticket, int usuario) throws SQLException{
		CallableStatement cl = conexion.prepareCall(" { call Atendiendo_ticket (?, ? ) } ");
		cl.setInt(1, ticket);
		cl.setInt(2, usuario);
		cl.execute();
	}
	
	public void FinalizaTicket (int ticket, String comentario, int usuario) throws SQLException {
		CallableStatement cl = conexion.prepareCall(" { call Finalizar (?, ?, ?) } ");
		cl.setInt(1, ticket);
		cl.setString(2, comentario);
		cl.setInt(3, usuario);
		cl.execute();
	}
	
	
	
	
	
	public void RegistraDatos(ArrayList<String> datos) throws SQLException {
		String insert = "insert into tarea (nombre, apellido, correo, telefono, edad, ciudad, pais, comentario, genero, interes)\r\n"
				+ "values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?); ";
		PreparedStatement ps = conexion.prepareStatement(insert);
		ps.setString(1, datos.get(0)); // nombre
		ps.setString(2, datos.get(1)); // apellido
		ps.setString(3, datos.get(2)); // correo
		ps.setInt(4, Integer.parseInt(datos.get(3))); // telefono
		ps.setInt(5, Integer.parseInt(datos.get(4))); // edad
		ps.setString(6, datos.get(5)); // ciudad
		ps.setString(7, datos.get(6)); // pais
		ps.setString(8, datos.get(7));
		ps.setString(9, datos.get(8));
		ps.setString(10, datos.get(9));
		ps.execute();
		ps.close();
	}

}