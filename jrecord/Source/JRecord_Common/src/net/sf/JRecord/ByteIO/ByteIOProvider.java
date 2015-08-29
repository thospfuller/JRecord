/*
 * @Author Bruce Martin
 * Created on 29/08/2005
 *
 * Purpose:
 *
 * Modification log:
 * On 2006/06/28 by Jean-Francois Gagnon:
 *    - Added a Fujitsu Variable Length Line IO Provider
 *
 * # Version 0.60 Bruce Martin 2007/02/16
 *   - Started work on seperating Record section out, so removing
 *     all reference to the Common module and used a new Constants
 *     module
 */
package net.sf.JRecord.ByteIO;

import java.io.UnsupportedEncodingException;

import net.sf.JRecord.Common.Constants;
import net.sf.JRecord.Common.IBasicFileSchema;

/**
 * LineIOprovider - This class returns a LineIO class appropriate for
 * the supplied file structure
 *
 * @author Bruce Martin
 * @version 0.55
 *
 */
public class ByteIOProvider {

    private static ByteIOProvider ioProvider = null;
    private static final int DEFAULT_RECORD_LENGTH = 80;



    /**
     * Gets a Record Reader Class that is appropriate for reading the
     * supplied file-structure
     *
     * @param fileStructure File Structure of the required reader
     *
     * @return line reader
     * @deprecated for use inside JRecord/RecordEditor, use {@link ByteIOProvider#getByteReader(IBasicFileSchema)}
     */
    public AbstractByteReader getByteReader(int fileStructure) {
        return getByteReader(fileStructure, DEFAULT_RECORD_LENGTH);
    }

    
    /**
     * Get ByteReader for schema
     * @param schema File schema to get the reader for
     * @return requested byte reader
     */
    public AbstractByteReader getByteReader(IBasicFileSchema schema) {
    	int fileStructure = schema.getFileStructure();
		if (fileStructure == Constants.IO_BIN_TEXT) {
    		return new ByteTextReader(schema.getFontName());
    	}
        return getByteReader(fileStructure, schema.getMaximumRecordLength());
    }

    /**
     * Gets a Record Reader Class that is appropriate for writing the
     * supplied file-structure
     *
     * @param fileStructure File Structure of the required reader
     * @param length length (if a Fixed length Provider)
     *
     * @return line reader
     * @deprecated for use inside JRecord/RecordEditor, use {@link ByteIOProvider#getByteReader(IBasicFileSchema)}
     */
    public AbstractByteReader getByteReader(int fileStructure, int length) {

       	switch(fileStructure) {
       		case Constants.IO_FIXED_LENGTH:		return new FixedLengthByteReader(length);
			case Constants.IO_VB: 				return new VbByteReader(false, true);
			case Constants.IO_VB_DUMP:			return new VbDumpByteReader();
			case Constants.IO_VB_FUJITSU:		return new FujitsuVbByteReader();
			case Constants.IO_VB_OPEN_COBOL:	return new VbByteReader(false, false);
			case Constants.IO_BIN_TEXT:			return new ByteTextReader();
			case Constants.IO_MICROFOCUS:		return new MicroFocusByteReader();
	    }
        return null;
    }


    /**
     * Gets a Record Reader Class
     *
     * @param fileStructure File Structure
     *
     * @return record reader
     */
    public AbstractByteWriter getByteWriter(int fileStructure) {
    	return getByteWriter(fileStructure, null);
    }

    /**
     * Get the appropriate writer for a basic-schema
     * @param schema
     * @return requested byte writer
     */
    public AbstractByteWriter getByteWriter(IBasicFileSchema schema) {
    	int fileStructure = schema.getFileStructure();
		switch(fileStructure) {
		case Constants.IO_FIXED_LENGTH:	return new FixedLengthByteWriter(schema.getMaximumRecordLength());
    	}
    	return getByteWriter(fileStructure, schema.getFontName());
    }

    /**
     * Gets a Record Reader Class
     *
     * @param fileStructure File Structure
     *
     * @return record reader
     */
    public AbstractByteWriter getByteWriter(int fileStructure, String charcterSet) {

    	switch(fileStructure) {
//    		case Constants.IO_FIXED_LENGTH:			return new BinaryByteWriter();
    		case Constants.IO_VB: 					return new VbByteWriter();
    		case Constants.IO_VB_DUMP:				return new VbDumpByteWriter();
    		case Constants.IO_VB_FUJITSU:			return new FujitsuVbByteWriter();
    		case Constants.IO_VB_OPEN_COBOL:		return new VbByteWriter(false);
			case Constants.IO_BIN_TEXT:
				if (charcterSet != null && charcterSet.length() > 0 ) {
					try {
						return new ByteTextWriter("\n".getBytes(charcterSet));
					} catch (UnsupportedEncodingException e) {
					}
				}
				return new ByteTextWriter();
//    		case (Constants.IO_BIN_TEXT):			return new FixedLengthByteWriter(false, false, Constants.);
        }

        return null;
    }


    /**
     * Get an instance of LineIOProvider
     * @return a LineIOProvider
     */
    public static ByteIOProvider getInstance() {
        if (ioProvider == null) {
            ioProvider = new ByteIOProvider();
        }

        return ioProvider;
    }

    /**
     * Set the system lineIOprovider
     * @param newProvider new IO provider
     */
    public static void setInstance(ByteIOProvider newProvider) {
        ioProvider = newProvider;
    }
}