package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jdk.nashorn.internal.ir.RuntimeNode.Request;
import DBUtil.DBUtil;


public class servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    
    public servlet() 
    {
        super();
        
    }

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		
		PrintWriter out=response.getWriter();
		response.setContentType("text/html");
		
		if(request.getParameter("confirm").equals("Reset")||request.getParameter("confirm").equals("Ready"))
		{
				DBTruncate();
				servlet.view(request,response,0);
		}
		/*else if(request.getParameter("confirm").equals("Reset"))
		{
			System.out.println("reset");
			DBTruncate();
			servlet.view(request,response,0);
		}*/
		else if(request.getParameter("confirm").equals("OK"))
		{
			if(check()==2)
				response.sendRedirect("comwon.html");
			else if(check()==1)
				response.sendRedirect("youwon.html");
			servlet.view(request,response,1);
		}
		else 
		{
			
			if(check()==2)
				response.sendRedirect("comwon.html");
			else if(check()==1)
				response.sendRedirect("youwon.html");
			else
				servlet.view(request,response,1);
		}	
			
	}
	
	public static int check() 
	{
		Connection c=DBUtil.getDBConnection();
		PreparedStatement ps;
		String possible[]=new String[]{"123","456","789","147","258","369","159","357"};
		try 
		{
			String sb1=new String();
			String sb2=new String();
			ps = c.prepareStatement("select * from game");
			ResultSet rs=ps.executeQuery();
			while(rs.next())
				sb1+=(rs.getString(1));
			for(int i=0;i<8;i++)
					if(sb1.contains(possible[i].charAt(0)+"")&&sb1.contains(possible[i].charAt(1)+"")&&sb1.contains(possible[i].charAt(2)+""))
						return 1;
			System.out.println(sb1);
			
			ps = c.prepareStatement("select * from computer");
			ResultSet rs1=ps.executeQuery();
			while(rs1.next())
				sb2+=(rs1.getString(1));
			for(int i=0;i<8;i++)
			if(sb2.contains(possible[i].charAt(0)+"")&&sb2.contains(possible[i].charAt(1)+"")&&sb2.contains(possible[i].charAt(2)+""))
				return 2;
			System.out.println(sb2);
			
			//ps=c.prepareStatement("truncate table computer");
			ps.execute();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}

		return 0;
	}


	public static void DBTruncate() 
	{
		Connection c=DBUtil.getDBConnection();
		PreparedStatement ps;
		try 
		{
			ps = c.prepareStatement("truncate table game");
			ps.execute();
			ps=c.prepareStatement("truncate table computer");
			ps.execute();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}


	public static String addButton(String button)
	{
		Connection c=DBUtil.getDBConnection();
		PreparedStatement ps;
		try 
		{
			ps = c.prepareStatement("insert into game values(?)");
			ps.setString(1, button);
			int row=ps.executeUpdate();
			if(row>0)
				return "success";
			else
				return "fail";
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return "fail";
		}
		
		
	}
	
	
	public static String getButton(String button)
	{
		Random random=new Random();
		int ran=random.nextInt(9);
		Connection c=DBUtil.getDBConnection();
		PreparedStatement ps;
		int k=0,l=0;
		try 
		{
			ps = c.prepareStatement("select * from game");
			ResultSet rs=ps.executeQuery();
			
			while(rs.next())
			{
				String s=rs.getString(1);
				if(s.equals(button))
				{//	System.out.println(s+" "+button+" "+ran);
					k++;
					//if(String.valueOf(ran).equals(s))
					//	ran=random.nextInt(9);
					//else
						//l++;
					break;
				}
				
			}
			ps = c.prepareStatement("select * from computer");
			ResultSet rs1=ps.executeQuery();
			
			while(rs1.next())
			{
				String s=rs1.getString(1);
				if(s.equals(button))
				{	//System.out.println(s+" "+button+" "+ran);
					l++;
					//if(String.valueOf(ran).equals(s))
					//	ran=random.nextInt(9);
					//else
						//l++;
					break;
				}
				
			}
			
			if(k!=0)
				return "red";
			else if(l!=0)
				return "green";
			else
				return "aqua";
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return "aqua";
		}
	}
	
	
	public static void view(HttpServletRequest request,HttpServletResponse response,int val) throws IOException
	{
			if(val!=0)
			{
				Random ran=new Random();
				servlet.addButton(request.getParameter("r"));
				servlet.addcom(ran.nextInt(9));
			//	servlet.addcom1();
			}
		PrintWriter out=response.getWriter();
		out.print("<html><head><title>XOGAME</title></head>");
		out.print("<body bgcolor=\"#378973\"><form method=\"post\"");
		if(check()==2)
			out.print("action=\"comwon.html\"");
		else if(check()==1)
			out.print("action=\"youwon.html\"");
		else
			out.print( " action=\"servlet\"" );
		out.print("><br><br><br>");
		//out.print(request.getParameter("r"));
		out.print("<h2 align=\"center\">X0 GAME</h2>");
		out.print("<table align=\"center\" cellspacing=\"10\" border=\"2\"><tr>");
		
		for(int i=1;i<=9;i++)
		{
			out.print("<td bgcolor=\""+servlet.getButton(String.valueOf(i))+"\">");
			out.print("<input type=\"radio\" name=\"r\" value=\""+i+"\"></td>");
			out.print(i%3==0?"</tr><br><tr>":"");
		}	
		out.print("</table><table align=\"center\"><tr><td>");
		out.print("<input type=\"submit\" name=\"confirm\" value=\"OK\"  ></td>"
				+ "</tr></table>");
	}


	public static void addcom(int button) 
	{
		if(button==0)
			addcom(new Random().nextInt(9));
		else{
		Connection c=DBUtil.getDBConnection();
		PreparedStatement ps;
		try 
		{
			ps=c.prepareStatement("select * from game");
			ResultSet rs=ps.executeQuery();
			while(rs.next())
			{
				if(rs.getString(1).equals(String.valueOf(button)))
					addcom(new Random().nextInt(9));
			}
			
			ps=c.prepareStatement("select * from computer");
			ResultSet rs1=ps.executeQuery();
			while(rs1.next())
			{
				if(rs1.getString(1).equals(String.valueOf(button)))
					addcom(new Random().nextInt(9));
			}
			
			
			ps = c.prepareStatement("insert into computer values(?)");
			ps.setString(1, String.valueOf(button));
			int row=ps.executeUpdate();
			if(row==0)
				addcom(new Random().nextInt(9));
				
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		}
	
	}
	public static void addcom1() 
	{
		int button = 0,b=0;
		String arr[]=new String[]{"123","456","789","147","258","369","159","357"};
		StringBuffer possible[]=new StringBuffer[arr.length];
		for(String s:arr)
			possible[b++]=new StringBuffer(s);
		Connection c=DBUtil.getDBConnection();
		PreparedStatement ps;
		try 
		{
			ps=c.prepareStatement("select * from game");
			ResultSet rs=ps.executeQuery();
			while(rs.next())
			{
				for(int i=0;i<8;i++)
				{
					if(new String(possible[i]).contains(rs.getString(1)+"")){
						button=Integer.parseInt(possible[i].charAt(new Random().nextInt(2))+"");
						break;}
						//possible[i].delete(0, possible[i].length());
				}
				for(StringBuffer s:possible)
				{
					System.out.println(s);
					if(!s.equals(""))
					{
					// button=Integer.parseInt(s.charAt(1)+"");
					 break;
					 }
				}
					//addcom(Integer.parseInt(s.charAt(new Random().nextInt(3))+""));
			}
			
			ps=c.prepareStatement("select * from computer");
			ResultSet rs1=ps.executeQuery();
			while(rs1.next())
			{
				if(rs1.getString(1).equals(String.valueOf(button)))
					addcom1();
			}
			
			
			
			
			
			ps = c.prepareStatement("insert into computer values(?)");
			ps.setString(1, String.valueOf(button));
			int row=ps.executeUpdate();
			if(row==0)
				addcom(new Random().nextInt(9));
				
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	
	}

	
}
