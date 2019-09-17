/*
 * Copyright (C) 2013-2015 RoboVM AB
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.robovm.apple.ext.corenfc;

/*<imports>*/
import java.io.*;
import java.nio.*;
import java.util.*;
import org.robovm.objc.*;
import org.robovm.objc.annotation.*;
import org.robovm.objc.block.*;
import org.robovm.rt.*;
import org.robovm.rt.annotation.*;
import org.robovm.rt.bro.*;
import org.robovm.rt.bro.annotation.*;
import org.robovm.rt.bro.ptr.*;
import org.robovm.apple.foundation.*;
import org.robovm.apple.dispatch.*;
/*</imports>*/

/*<javadoc>*/
/**
 * @since Available in iOS 11.0 and later.
 */
/*</javadoc>*/
/*<annotations>*//*</annotations>*/
/*<visibility>*/public/*</visibility>*/ interface /*<name>*/NFCISO15693Tag/*</name>*/ 
    /*<implements>*/extends NFCTag, NFCNDEFTag/*</implements>*/ {

    /*<ptr>*/
    /*</ptr>*/
    /*<bind>*/
    /*</bind>*/
    /*<constants>*//*</constants>*/
    /*<properties>*/
    @Property(selector = "identifier")
    NSData getIdentifier();
    @Property(selector = "icManufacturerCode")
    @MachineSizedUInt long getIcManufacturerCode();
    @Property(selector = "icSerialNumber")
    NSData getIcSerialNumber();
    /*</properties>*/
    /*<methods>*/
    /**
     * @since Available in iOS 11.0 and later.
     */
    @Method(selector = "sendCustomCommandWithConfiguration:completionHandler:")
    void sendCustomCommand(NFCISO15693CustomCommandConfiguration commandConfiguration, @Block VoidBlock2<NSData, NSError> completionHandler);
    /**
     * @since Available in iOS 11.0 and later.
     */
    @Method(selector = "readMultipleBlocksWithConfiguration:completionHandler:")
    void readMultipleBlocks(NFCISO15693ReadMultipleBlocksConfiguration readConfiguration, @Block VoidBlock2<NSData, NSError> completionHandler);
    /**
     * @since Available in iOS 13.0 and later.
     */
    @Method(selector = "stayQuietWithCompletionHandler:")
    void stayQuietWithCompletionHandler(@Block VoidBlock1<NSError> completionHandler);
    /**
     * @since Available in iOS 13.0 and later.
     */
    @Method(selector = "readSingleBlockWithRequestFlags:blockNumber:completionHandler:")
    void readSingleBlockWithRequestFlags$blockNumber$completionHandler$(RequestFlag flags, byte blockNumber, @Block VoidBlock2<NSData, NSError> completionHandler);
    /**
     * @since Available in iOS 13.0 and later.
     */
    @Method(selector = "writeSingleBlockWithRequestFlags:blockNumber:dataBlock:completionHandler:")
    void writeSingleBlockWithRequestFlags$blockNumber$dataBlock$completionHandler$(RequestFlag flags, byte blockNumber, NSData dataBlock, @Block VoidBlock1<NSError> completionHandler);
    /**
     * @since Available in iOS 13.0 and later.
     */
    @Method(selector = "lockBlockWithRequestFlags:blockNumber:completionHandler:")
    void lockBlockWithRequestFlags$blockNumber$completionHandler$(RequestFlag flags, byte blockNumber, @Block VoidBlock1<NSError> completionHandler);
    /**
     * @since Available in iOS 13.0 and later.
     */
    @Method(selector = "readMultipleBlocksWithRequestFlags:blockRange:completionHandler:")
    void readMultipleBlocksWithRequestFlags$blockRange$completionHandler$(RequestFlag flags, @ByVal NSRange blockRange, @Block VoidBlock2<NSArray<NSData>, NSError> completionHandler);
    /**
     * @since Available in iOS 13.0 and later.
     */
    @Method(selector = "writeMultipleBlocksWithRequestFlags:blockRange:dataBlocks:completionHandler:")
    void writeMultipleBlocksWithRequestFlags$blockRange$dataBlocks$completionHandler$(RequestFlag flags, @ByVal NSRange blockRange, NSArray<NSData> dataBlocks, @Block VoidBlock1<NSError> completionHandler);
    /**
     * @since Available in iOS 13.0 and later.
     */
    @Method(selector = "selectWithRequestFlags:completionHandler:")
    void selectWithRequestFlags$completionHandler$(RequestFlag flags, @Block VoidBlock1<NSError> completionHandler);
    /**
     * @since Available in iOS 13.0 and later.
     */
    @Method(selector = "resetToReadyWithRequestFlags:completionHandler:")
    void resetToReadyWithRequestFlags$completionHandler$(RequestFlag flags, @Block VoidBlock1<NSError> completionHandler);
    /**
     * @since Available in iOS 13.0 and later.
     */
    @Method(selector = "writeAFIWithRequestFlag:afi:completionHandler:")
    void writeAFIWithRequestFlag$afi$completionHandler$(RequestFlag flags, byte afi, @Block VoidBlock1<NSError> completionHandler);
    /**
     * @since Available in iOS 13.0 and later.
     */
    @Method(selector = "lockAFIWithRequestFlag:completionHandler:")
    void lockAFIWithRequestFlag$completionHandler$(RequestFlag flags, @Block VoidBlock1<NSError> completionHandler);
    /**
     * @since Available in iOS 13.0 and later.
     */
    @Method(selector = "writeDSFIDWithRequestFlag:dsfid:completionHandler:")
    void writeDSFIDWithRequestFlag$dsfid$completionHandler$(RequestFlag flags, byte dsfid, @Block VoidBlock1<NSError> completionHandler);
    /**
     * @since Available in iOS 13.0 and later.
     */
    @Method(selector = "lockDFSIDWithRequestFlag:completionHandler:")
    void lockDFSIDWithRequestFlag$completionHandler$(RequestFlag flags, @Block VoidBlock1<NSError> completionHandler);
    /**
     * @since Available in iOS 13.0 and later.
     */
    @Method(selector = "getSystemInfoWithRequestFlag:completionHandler:")
    void getSystemInfoWithRequestFlag$completionHandler$(RequestFlag flags, @Block("(@MachineSizedSInt,@MachineSizedSInt,@MachineSizedSInt,@MachineSizedSInt,@MachineSizedSInt,)") VoidBlock6<Long, Long, Long, Long, Long, NSError> completionHandler);
    /**
     * @since Available in iOS 13.0 and later.
     */
    @Method(selector = "getMultipleBlockSecurityStatusWithRequestFlag:blockRange:completionHandler:")
    void getMultipleBlockSecurityStatusWithRequestFlag$blockRange$completionHandler$(RequestFlag flags, @ByVal NSRange blockRange, @Block VoidBlock2<NSArray<NSNumber>, NSError> completionHandler);
    /**
     * @since Available in iOS 13.0 and later.
     */
    @Method(selector = "customCommandWithRequestFlag:customCommandCode:customRequestParameters:completionHandler:")
    void customCommandWithRequestFlag$customCommandCode$customRequestParameters$completionHandler$(RequestFlag flags, @MachineSizedSInt long customCommandCode, NSData customRequestParameters, @Block VoidBlock2<NSData, NSError> completionHandler);
    /**
     * @since Available in iOS 13.0 and later.
     */
    @Method(selector = "extendedReadSingleBlockWithRequestFlags:blockNumber:completionHandler:")
    void extendedReadSingleBlockWithRequestFlags$blockNumber$completionHandler$(RequestFlag flags, @MachineSizedSInt long blockNumber, @Block VoidBlock2<NSData, NSError> completionHandler);
    /**
     * @since Available in iOS 13.0 and later.
     */
    @Method(selector = "extendedWriteSingleBlockWithRequestFlags:blockNumber:dataBlock:completionHandler:")
    void extendedWriteSingleBlockWithRequestFlags$blockNumber$dataBlock$completionHandler$(RequestFlag flags, @MachineSizedSInt long blockNumber, NSData dataBlock, @Block VoidBlock1<NSError> completionHandler);
    /**
     * @since Available in iOS 13.0 and later.
     */
    @Method(selector = "extendedLockBlockWithRequestFlags:blockNumber:completionHandler:")
    void extendedLockBlockWithRequestFlags$blockNumber$completionHandler$(RequestFlag flags, @MachineSizedSInt long blockNumber, @Block VoidBlock1<NSError> completionHandler);
    /**
     * @since Available in iOS 13.0 and later.
     */
    @Method(selector = "extendedReadMultipleBlocksWithRequestFlags:blockRange:completionHandler:")
    void extendedReadMultipleBlocksWithRequestFlags$blockRange$completionHandler$(RequestFlag flags, @ByVal NSRange blockRange, @Block VoidBlock2<NSArray<NSData>, NSError> completionHandler);
    /*</methods>*/
    /*<adapter>*/
    /*</adapter>*/
}
