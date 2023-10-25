package com.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.io.*;



import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Servlet implementation class XmlParserServlet
 */
public class XmlParserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public XmlParserServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @throws IOException 
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
    
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		        	
		String xmlData = "<Customer>\n"			
						+ "<customerİd>123</customerİd>\n"				
						+ "<pidNo>99999999999</pidNo>\n"				
						+ "<taxNo></taxNo>\n"				
						+ "<firstName>xml</firstName>\n"				
						+ "<lastName>test</lastName>\n"				
						+ "<gender>E</gender>\n"				
						+ "<birthDate>01/01/2000</birthDate>\n"				
						+ "<status>true</status>\n"
						+ "</Customer>";
		
		response.getWriter().write(xmlData);
		
		try {
			
			Object data = parseXmlToModel("com.model.Customer", xmlData);
			
			Class<?> model = data.getClass();
			
			for (Field field : model.getDeclaredFields()) {
				
				String fieldName = field.getName();
				
				String fieldGetMethod = "get" + fieldName.substring(0,1).toUpperCase() + fieldName.substring(1);
				
				Object value = model.getMethod(fieldGetMethod).invoke(data);
				
				response.getWriter().write(fieldName + " : " + value + "\n");
			}
			
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | NoSuchMethodException | SecurityException | NoSuchFieldException
				| ParserConfigurationException | SAXException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private Object parseXmlToModel(String className, String xmlData) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ParserConfigurationException, SAXException, IOException, NoSuchFieldException {
		
		Class<?> clazz = Class.forName(className);
		Object instance = clazz.getDeclaredConstructor().newInstance();
				
		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();	
		InputSource is = new InputSource(new StringReader(xmlData));	
		org.w3c.dom.Document doc =  (org.w3c.dom.Document) builder.parse(is);		
		
		NodeList nodeList = doc.getDocumentElement().getChildNodes();
				
		for (int i = 0; i < nodeList.getLength(); i++) {
			
            Node fieldNode = nodeList.item(i);
            
            if (fieldNode.getNodeType() == Node.ELEMENT_NODE) {
            	
                String fieldName = fieldNode.getNodeName();
                String fieldValue = fieldNode.getTextContent();
                Object fieldValueFinal = null;
                  
                Field field = instance.getClass().getDeclaredField(fieldName); 
                
                Type fieldType = field.getGenericType();
                
                if(fieldType == int.class && fieldValue != "") {
                	fieldValueFinal = Integer.parseInt(fieldValue);
                }
                else if (fieldType == Long.class && fieldValue != "") {
                	fieldValueFinal = Long.parseLong(fieldValue);
				}
                else if (fieldType == boolean.class && fieldValue != "") {
                	fieldValueFinal = Boolean.parseBoolean(fieldName);
				}
                else if (fieldType == String.class) {
                	fieldValueFinal = fieldValue;
				}
                
                field.setAccessible(true);               
                field.set(instance, fieldValueFinal);

            }
        }
		
		return instance;
		
	}

}
