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
package org.compiere.model;

import java.io.*;
import java.sql.*;
import java.util.logging.*;

import org.compiere.framework.*;
import org.compiere.util.*;

/**
 *  Info Class for Lookup SQL (ValueObject)
 *
 * 	@author 	Jorg Janke
 * 	@version 	$Id: MLookupInfo.java,v 1.3 2006/07/30 00:58:37 jjanke Exp $
 */
public class MLookupInfo implements Serializable, Cloneable
{
	/**
	 *  Get first AD_Reference_ID of a matching Reference Name.
	 *  Can have SQL LIKE place holders.
	 *  (This is more a development tool than used for production)
	 *  @param referenceName reference name
	 *  @return AD_Reference_ID
	 */
	public static int getAD_Reference_ID (String referenceName)
	{
		int retValue = 0;
		String sql = "SELECT AD_Reference_ID,Name,ValidationType,IsActive "
			+ "FROM AD_Reference WHERE Name LIKE ?";
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setString(1, referenceName);
			ResultSet rs = pstmt.executeQuery();
			//
			int i = 0;
			int id = 0;
			String refName = "";
			String validationType = "";
			boolean isActive = false;
			while (rs.next())
			{
				id = rs.getInt(1);
				if (i == 0)
					retValue = id;
				refName = rs.getString(2);
				validationType = rs.getString(3);
				isActive = rs.getString(4).equals("Y");
				CLogger.get().config("AD_Reference Name=" + refName + ", ID=" + id + ", Type=" + validationType + ", Active=" + isActive);
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			CLogger.get().log(Level.SEVERE, sql, e);
		}
		return retValue;
	}   //  getAD_Reference_ID

	/**
	 *  Get first AD_Column_ID of matching ColumnName.
	 *  Can have SQL LIKE place holders.
	 *  (This is more a development tool than used for production)
	 *  @param columnName column name
	 *  @return AD_Column_ID
	 */
	public static int getAD_Column_ID (String columnName)
	{
		int retValue = 0;
		String sql = "SELECT c.AD_Column_ID,c.ColumnName,t.TableName "
			+ "FROM AD_Column c, AD_Table t "
			+ "WHERE c.ColumnName LIKE ? AND c.AD_Table_ID=t.AD_Table_ID";
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setString(1, columnName);
			ResultSet rs = pstmt.executeQuery();
			//
			int i = 0;
			int id = 0;
			String colName = "";
			String tabName = "";
			while (rs.next())
			{
				id = rs.getInt(1);
				if (i == 0)
					retValue = id;
				colName = rs.getString(2);
				tabName = rs.getString(3);
				CLogger.get().config("Name=" + colName + ", ID=" + id + ", Table=" + tabName);
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			CLogger.get().log(Level.SEVERE, sql, e);
		}
		return retValue;
	}   //  getAD_Column_ID
	
	
	/**************************************************************************
	 *  Constructor.
	 * 	(called from MLookupFactory)
	 *  @param sqlQuery SQL query
	 *  @param tableName table name
	 *  @param keyColumn key column
	 *  @param zoomWindow zoom window
	 *  @param zoomWindowPO PO zoom window
	 *  @param zoomQuery zoom query
	 */
	public MLookupInfo (String sqlQuery, String tableName, String keyColumn, 
		int zoomWindow, int zoomWindowPO, Query zoomQuery)
	{
		if (sqlQuery == null)
			throw new IllegalArgumentException("SqlQuery is null");
		Query = sqlQuery;
		if (keyColumn == null)
			throw new IllegalArgumentException("KeyColumn is null");
		TableName = tableName;
		KeyColumn = keyColumn;
		ZoomWindow = zoomWindow;
		ZoomWindowPO = zoomWindowPO;
		ZoomQuery = zoomQuery;
	}   //  MLookupInfo
	
	static final long serialVersionUID = -7958664359250070233L;

	/** SQL Query       */
	private String		Query = null;
	/** Table Name      */
	public String       TableName = "";
	/** Key Column      */
	public String       KeyColumn = "";
	/** Zoom Window     */
	public int          ZoomWindow;
	/** Zoom Window     */
	public int          ZoomWindowPO;
	/** Zoom Query      */
	public Query		ZoomQuery = null;

	/** Direct Access Query 	*/
	public String       QueryDirect = "";
	/** Parent Flag     */
	public boolean      IsParent = false;
	/** Key Flag     	*/
	public boolean      IsKey = false;
	/** Validation code */
	public String       ValidationCode = "";
	/** Validation flag */
	private boolean		IsValidated = false;
	
	/**	AD_Column_Info or AD_Process_Para	*/
	public int          Column_ID;
	/** Real AD_Reference_ID				*/
	public int 			AD_Reference_Value_ID;
	/** CreadedBy?updatedBy					*/
	public boolean		IsCreadedUpdatedBy = false;
	

	/**
	 * 	Set Validation Flag
	 *	@param validated validated
	 */
	public void setValidated (boolean validated)
	{
		IsValidated = validated;
	}	//	setValidated
	
	/**
	 * 	Lookup Info Validated
	 *	@return true if validated
	 */
	public boolean isValidated()
	{
		return IsValidated;
	}	//	isValidated
	
	/**
	 * 	Get Query
	 *	@return query
	 */
	public String getQuery()
	{
		return Query;
	}	//	getQuery
	
	/**
	 * 	Set Query
	 *	@param newQuery new query
	 */
	public void setQuery (String newQuery)
	{
		Query = newQuery;
	}	//	setQuery
	
	/**
	 * String representation
	 * @return info
	 */
	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer ("MLookupInfo[")
			.append(KeyColumn)
			.append("-Direct=").append(QueryDirect)
			.append("]");
		return sb.toString();
	}	//	toString

	/**
	 * 	Clone
	 *	@return deep copy
	 */
	public MLookupInfo cloneIt()
	{
		try
		{
			MLookupInfo clone = (MLookupInfo)super.clone();
			return clone;
		}
		catch (Exception e)
		{
			CLogger.get().log(Level.SEVERE, "", e);
		}
		return null;
	}	//	clone

}   //  MLookupInfo