/******************************************************************************
 * Product: Compiere ERP & CRM Smart Business Solution                        *
 * Copyright (C) 1999-2007 ComPiere, Inc. All Rights Reserved.                *
 * This program is free software, you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY, without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program, if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ComPiere, Inc., 3600 Bridge Parkway #102, Redwood City, CA 94065, USA      *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
package org.compiere.util;

import java.io.*;

import org.apache.ecs.*;
import org.apache.ecs.xhtml.*;


/**
 *  XHTML Document.
 *
 *  @author Jorg Janke
 *  @version  $Id: WebDoc.java,v 1.2 2006/07/30 00:51:05 jjanke Exp $
 */
public class WebDoc
{
	/**
	 *  Create styled Document with Title
	 *  @param plain if true adds standard.css and standard.js
	 *  @param title optional header title and h1 
	 *  @param javaClient true if Java Client - browser otherwise
	 *  @return Document
	 */
	public static WebDoc create (boolean plain, String title, boolean javaClient)
	{
		WebDoc doc = new WebDoc();
		doc.setUp (plain, javaClient, title);
		return doc;
	}   //  create
	
	/**
	 *  Create Document
	 *  @param plain if true adds stylesheet and standard js
	 *  @return Document
	 */
	public static WebDoc create (boolean plain)
	{
		return create (plain, null, false);
	}   //  create
	
	/**
	 *  Create styled popup Document with Title
	 *  @param title header title and h1 
	 *  @return Document
	 */
	public static WebDoc createPopup (String title)
	{
		WebDoc doc = create (title);
		doc.getHead().addElement(new script((Element)null, "window.js"));
		doc.getHead().addElement(new link("popup.css", link.REL_STYLESHEET, link.TYPE_CSS));
		doc.setClasses ("popupTable", "popupHeader");
		doc.getTable().setCellSpacing(5);
		return doc;
	}   //  createPopup

	/**
	 *  Create styled window Document with Title
	 *  @param title header title and h1 
	 *  @return Document
	 */
	public static WebDoc createWindow (String title)
	{
		WebDoc doc = create (title);
		doc.getHead().addElement(new link("window.css", link.REL_STYLESHEET, link.TYPE_CSS));
		doc.getHead().addElement(new script((Element)null, "window.js"));
		doc.setClasses ("windowTable", "windowHeader");
		doc.getTable().setCellSpacing(5);
		return doc;
	}   //  createWindow

	/**
	 *  Create styled web Document with Title
	 *  @param title optional header title and h1 
	 *  @return Document
	 */
	public static WebDoc create (String title)
	{
		return create (false, title, false);
	}   //  create

	/** Non brealing Space					*/
	public static final String	NBSP	= "&nbsp;";
	
	
	/**************************************************************************
	 *  Create new XHTML Document structure
	 */
	private WebDoc ()
	{
	}   //  WDoc

	private html    m_html = new html();
	private head    m_head = new head();
	private body    m_body = new body();
	private table	m_table = null;
	private tr		m_topRow = null;
	private td		m_topRight = null;
	private td		m_topLeft = null;

	/**
	 *  Set up Document
	 *  @param plain if true adds stylesheet and standard js
	 *  @param javaClient true if Java Client - browser otherwise
	 *  @param title header title and h1
	 */
	private void setUp (boolean plain, boolean javaClient, String title)
	{
		m_html.addElement(m_head);
		m_html.addElement(m_body);
		m_body.addElement(new a().setName("top"));
		if (title != null)
			m_head.addElement(new title(title));
		if (plain)
			return;
		
		//	css, js
		if (javaClient)
			m_head.addElement(new link("http://www.compiere.org/standard.css", link.REL_STYLESHEET, link.TYPE_CSS));
		else
		{
			m_head.addElement(new link(WebEnv.getStylesheetURL(), link.REL_STYLESHEET, link.TYPE_CSS));
			m_head.addElement(new script((Element)null, WebEnv.getBaseDirectory("standard.js")));
		}
		m_head.addElement(new meta().setHttpEquiv("Content-Type", "text/html; charset=UTF-8"));
		m_head.addElement(new meta().setName("description", "Compiere HTML UI"));

		m_table = new table("0", "2", "0", "100%", null);	//	spacing 2
		m_topRow = new tr();
		//	Title
		m_topLeft = new td();
		if (title == null)
			m_topLeft.addElement(NBSP);
		else
			m_topLeft.addElement(new h1(title));
		m_topRow.addElement(m_topLeft);
		//	Logo
		m_topRight = new td().setAlign("right");
		/** Removing/modifying the Compiere logo is a violation of the license	*/
		if (javaClient)
			m_topRight.addElement(new img("http://www.compiere.org/images/Compiere64x32.png")
				//	Changing the copyright notice in any way violates the license 
				//	and you'll be held liable for any damage claims
				.setAlign(AlignType.RIGHT).setAlt("&copy; Jorg Janke/Compiere"));
		else
			m_topRight.addElement(WebEnv.getLogo());
		m_topRow.addElement(m_topRight);
		m_table.addElement(m_topRow);
		//
		m_body.addElement(m_table);
	}   //  setUp

	/**
	 * 	Set css Classes
	 *	@param tableClass optional class for table
	 *	@param tdClass optional class for left/right td
	 */
	public void setClasses (String tableClass, String tdClass)
	{
		if (m_table != null && tableClass != null)
			m_table.setClass(tableClass);
		if (m_topLeft != null && tdClass != null)
			m_topLeft.setClass(tdClass);
		if (m_topRight != null && tdClass != null)
			m_topRight.setClass(tdClass);
	}	//	setClasses

	
	/**
	 *  Get Body
	 *  @return Body
	 */
	public body getBody()
	{
		return m_body;
	}   //  getBody

	/**
	 *  Get Head
	 *  @return Header
	 */
	public head getHead()
	{
		return m_head;
	}   //  getHead

	/**
	 * 	Get Table (no class set)
	 *	@return table
	 */
	public table getTable()
	{
		return m_table;
	}	//	getTable

	/**
	 * 	Get Table Row (no class set)
	 *	@return table row
	 */
	public tr getTopRow()
	{
		return m_topRow;
	}	//	getTopRow
	/**
	 * 	Get Table Data Left (no class set)
	 *	@return table data
	 */
	public td getTopLeft()
	{
		return m_topLeft;
	}	//	getTopLeft
	
	/**
	 * 	Get Table Data Right (no class set)
	 *	@return table data
	 */
	public td getTopRight()
	{
		return m_topRight;
	}	//	getTopRight
	
	/**
	 *  String representation
	 *  @return String
	 */
	@Override
	public String toString()
	{
		return m_html.toString();
	}   //  toString

	/**
	 *  Output Document
	 *  @param out out
	 */
	public void output (OutputStream out)
	{
		m_html.output(out);
	}   //  output

	/**
	 *  Output Document
	 *  @param out out
	 */
	public void output (PrintWriter out)
	{
		m_html.output(out);
	}   //  output

	/**
	 * 	Add Popup Center
	 * 	@param nowrap set nowrap in td
	 *	@return null or center single td
	 */
	public td addPopupCenter(boolean nowrap)
	{
		if (m_table == null)
			return null;
		//
		td center = new td ("popupCenter", AlignType.CENTER, AlignType.MIDDLE, nowrap);
		center.setColSpan(2);
		m_table.addElement(new tr()
			.addElement(center));
		return center;
	}	//	addPopupCenter

	/**
	 * 	Add Popup Close Footer
	 *	@return null or array with left/right td
	 */
	public td[] addPopupClose()
	{
		input button = WebUtil.createClosePopupButton(); 
		if (m_table == null)
		{
			m_body.addElement(button);
			return null;
		}
		//
		td left = new td("popupFooter", AlignType.LEFT, AlignType.MIDDLE, false, null);
		td right = new td("popupFooter", AlignType.RIGHT, AlignType.MIDDLE, false, button); 
		m_table.addElement(new tr()
			.addElement(left)
			.addElement(right));
		return new td[] {left, right};
	}	//	addPopupClose
	

	/**
	 * 	Add Window Center
	 * 	@param nowrap set nowrap in td
	 *	@return empty single center td
	 */
	public td addWindowCenter(boolean nowrap)
	{
		if (m_table == null)
			return null;
		//
		td center = new td ("windowCenter", AlignType.CENTER, AlignType.MIDDLE, nowrap);
		center.setColSpan(2);
		m_table.addElement(new tr()
			.addElement(center));
		return center;
	}	//	addWindowCenter

	/**
	 * 	Add Window Footer
	 *	@return null or array with empty left/right td
	 */
	public td[] addWindowFooters()
	{
		if (m_table == null)
			return null;
		//
		td left = new td("windowFooter", AlignType.LEFT, AlignType.MIDDLE, false);
		td right = new td("windowFooter", AlignType.RIGHT, AlignType.MIDDLE, false); 
		m_table.addElement(new tr()
			.addElement(left)
			.addElement(right));
		return new td[] {left, right};
	}	//	addWindowFooters

	/**
	 * 	Add Window Footer
	 *	@return empty single center td
	 */
	public td addWindowFooter()
	{
		if (m_table == null)
			return null;
		//
		td center = new td("windowFooter", AlignType.CENTER, AlignType.MIDDLE, false);
		m_table.addElement(new tr()
			.addElement(center));
		return center;
	}	//	addWindowFooter
	
	/**************************************************************************
	 *  Test Class
	 *  @param args args
	 */
	public static void main (String[] args)
	{
		WebDoc doc = WebDoc.create("Test");
		doc.getBody().addElement(new b("111 <<< >>> &&& \\\\ \u0100 �"));
		form f = new form("myaction");
		f.addElement(new input());
		doc.getBody().addElement(f);
		System.out.println(doc.toString());
		System.out.println("---------");
		doc.output(System.out);
		System.out.println("---------");
	}   //  main
}   //  WDoc
