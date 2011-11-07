package org.openecard.client.ifd.reader;

import java.util.Arrays;
import oasis.names.tc.dss._1_0.core.schema.Result;
import org.openecard.client.common.ECardConstants;
import org.openecard.client.common.util.CardCommandStatus;
import org.openecard.client.common.util.Helper;
import org.openecard.client.common.WSHelper;


/**
 *
 * @author Tobias Wich <tobias.wich@ecsec.de>
 */
public class ExecutePACEResponse {

    final int result;
    final short length;
    final byte[] data;

    public ExecutePACEResponse(byte[] response) {
	result = Helper.convertByteArrayToInt(Arrays.copyOfRange(response, 0, 4));
	length = (short) Helper.convertByteArrayToInt(new byte[]{response[5], response[4]});
	data   = Arrays.copyOfRange(response, 6, 6+length);
    }

    public boolean isError() {
	return result != 0;
    }

    public int getResultCode() {
	return result;
    }

    public Result getResult() {
	if (result == 0x00000000) return WSHelper.makeResultOK();
	// errors in input
	if (result ==  0xD0000001) return WSHelper.makeResultUnknownError("Inconsistent lengths in input.");
	if (result ==  0xD0000002) return WSHelper.makeResultUnknownError("Unexpected data in input.");
	if (result ==  0xD0000003) return WSHelper.makeResultUnknownError("Unexpected combination of data in input.");
	// errors in protocol
	if (result ==  0xE0000001) return WSHelper.makeResultUnknownError("Syntax error in TLV response.");
	if (result ==  0xE0000002) return WSHelper.makeResultUnknownError("Unexpected or missing object in TLV response.");
	if (result ==  0xE0000003) return WSHelper.makeResultUnknownError("Unknown PIN-ID.");
	if (result ==  0xE0000006) return WSHelper.makeResultUnknownError("Wrong Authentication Token.");
	// Response APDU of the card reports error
	if ((result & 0xFFFC0000) == 0xF0000000) {
	    byte[] sw = new byte[]{(byte)((result >> 8)&0xFF), (byte)(result&0xFF)};
	    String msg = CardCommandStatus.getMessage(sw);
	    int type = (result >> 16) & 0xFFFF;
	    if (type == 0xF000) return WSHelper.makeResultUnknownError("Select EF.CardAccess: " + msg);
	    if (type == 0xF001) return WSHelper.makeResultUnknownError("Read Binary EF.CardAccess: " + msg);
	    if (type == 0xF002) return WSHelper.makeResultUnknownError("MSE Set AT: " + msg);
	    if (type == 0xF003) return WSHelper.makeResultUnknownError("General Authenticat Step 1-4: " + msg);
	}
	// Others
	if (result ==  0xF0100001) return WSHelper.makeResultUnknownError("Communication abort.");
	if (result ==  0xF0100002) return WSHelper.makeResultError(ECardConstants.Minor.IFD.NO_CARD, "No card.");
	if (result ==  0xF0200001) return WSHelper.makeResultError(ECardConstants.Minor.IFD.CANCELLATION_BY_USER, "Abort.");
	if (result ==  0xF0200002) return WSHelper.makeResultError(ECardConstants.Minor.IFD.TIMEOUT_ERROR, "Timeout.");
	// unknown error
	return WSHelper.makeResultUnknownError(null);
    }

    public byte[] getData() {
	return data;
    }

}
