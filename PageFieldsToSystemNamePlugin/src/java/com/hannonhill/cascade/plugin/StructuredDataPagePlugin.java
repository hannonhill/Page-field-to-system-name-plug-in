package com.hannonhill.cascade.plugin;

import com.hannonhill.cascade.api.asset.common.Metadata;
import com.hannonhill.cascade.api.asset.common.DynamicMetadataField;
import com.hannonhill.cascade.api.asset.common.StructuredDataNode;
import com.cms.assetfactory.StructuredDataPlugin;

import java.util.Date;
import java.util.Calendar;
import java.util.Formatter;

/**
 * Abstract Plug-in class useful in searching out Page Field values based on provided String field
 * identifiers.  Extends the StructuredDataPlugin functionality to allow for retrieval of Wired and
 * Dynamic Metadata in addition to Structured Data (Data Definition) fields.<br/><br/>
 * 
 * The expected format of the field identifier Strings is as follows:<br/><br/>
 * 
 * Wired Metadata fields:  [field-name]  e.g. title,display-name,author<br/><br/>
 * 
 * Dynamic Metadata fields:  [dynamic-metadata/field-name]  e.g. dynamic-metadata/my-custom-field1,dynamic-metadata/my-custom-field2<br/><br/>
 * 
 * Structured Data (Data Definition) fields:  [system-data-structure/{group-name}/field-name]  e.g. system-data-structure/my-group/my-field,system-data-structure/my-ungrouped-field
 * 
 * @author Brent Arrington
 */
public abstract class StructuredDataPagePlugin extends StructuredDataPlugin
{
	protected final static String CUSTOM_METADATA_TOKEN = "dynamic-metadata";
	protected final static String STRUCTURED_DATA_TOKEN = "system-data-structure";
	/**
	 * Searches the provided Metadata for the given wired metadata field name (stIdentifier) and
	 * returns its value (if any).
	 * @param metadata Metadata object to be searched
	 * @param stIdentifier String indicating the specific metadata field name to search for
	 * @return String containing the value of the specified wired metadata field
	 */
	protected String searchWiredMetadata(Metadata metadata, String stIdentifier)
	{
		if (stIdentifier.contains("title"))
		{
			return metadata.getTitle().trim();
		}
		else if (stIdentifier.contains("display-name"))
		{
			return metadata.getDisplayName().trim();
		}
		else if (stIdentifier.contains("description"))
		{
			return metadata.getDescription().trim();
		}
		else if (stIdentifier.contains("author"))
		{
			return metadata.getAuthor().trim();
		}
		else if (stIdentifier.contains("keywords"))
		{
			return metadata.getKeywords().trim();
		}
		else if (stIdentifier.contains("summary"))
		{
			return metadata.getSummary().trim();
		}
		else if (stIdentifier.contains("teaser"))
		{
			return metadata.getTeaser().trim();
		}
		else if (stIdentifier.contains("start-date"))
		{
			// use date format of: yyyy-mm-dd
			Date startDate = metadata.getStartDate();
			Calendar cal = Calendar.getInstance();
			cal.setTime(startDate);
			Formatter format = new Formatter();
			String stDate = format.format("%tF", cal).toString();
			return stDate;
		}
		else if (stIdentifier.contains("end-date"))
		{
			// use date format of yyyy-mm-dd
			Date endDate = metadata.getEndDate();
			Calendar cal = Calendar.getInstance();
			cal.setTime(endDate);
			Formatter format = new Formatter();
			String stDate = format.format("%tF", cal).toString();
			return stDate;
		}
		else if (stIdentifier.contains("review-date"))
		{
			// use date format of yyyy-mm-dd
			Date reviewDate = metadata.getReviewDate();
			Calendar cal = Calendar.getInstance();
			cal.setTime(reviewDate);
			Formatter format = new Formatter();
			String stDate = format.format("%tF", cal).toString();
			return stDate;
		}
		else if (stIdentifier.contains("expiration-folder"))
		{
			return null;
		}
		return null;
	}
	
	/**
	 * Searches the provided array of dynamic metadata fields for the provided custom-field name (stIdentifier)
	 * and returns the value of said field.
	 * 
	 * @param dynamicFields DynamicMetadataFields[] array containin all custom fields to be searched
	 * @param stIdentifier String indicating the specific custom field name to search for
	 * @return String containing the value of the specified custom field
	 */
	protected String searchDynamicMetadata(DynamicMetadataField[] dynamicFields, String stIdentifier)
	{
		int startIndex = stIdentifier.indexOf(CUSTOM_METADATA_TOKEN) + CUSTOM_METADATA_TOKEN.length();
		String stNodeName = stIdentifier.substring(startIndex);
		
		if (stNodeName.startsWith("/"))
		{
			stNodeName = stNodeName.substring(1);
		}
		
		for (int i = 0; i < dynamicFields.length; i++)
		{
			if (dynamicFields[i].getName().equals(stNodeName))
			{
				return dynamicFields[i].getValues()[0].trim();
			}
		}
		
		return null;
	}
	
	/**
	 * Checks for the presence of <code>"system-data-strucure"</code> token in <code>sdIdentifier</code> & strips this out prior
	 * to passing along to <code>super.searchStructuredData(structuredData,sdIdentifier)</code>
	 * @see StructuredDataPlugin
	 */
	protected String searchStructuredData(StructuredDataNode[] structuredData, String sdIdentifier)
	{
		if (sdIdentifier.contains(STRUCTURED_DATA_TOKEN))
		{
			int startIndex = sdIdentifier.indexOf(STRUCTURED_DATA_TOKEN) + STRUCTURED_DATA_TOKEN.length();
			sdIdentifier = sdIdentifier.substring(startIndex);
		}
		
		return super.searchStructuredData(structuredData,sdIdentifier);
	}
}