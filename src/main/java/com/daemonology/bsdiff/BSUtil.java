/*
 * Copyright (C) 2016 THL A29 Limited, a Tencent company.
 * Copyright (c) 2005, Joe Desbonnet, (jdesbonnet@gmail.com)
 * Copyright 2003-2005 Colin Percival
 * All rights reserved
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted providing that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED.  IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
 * OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING
 * IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package com.daemonology.bsdiff;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static com.daemonology.bsdiff.BSDiff.bsdiff;
import static com.daemonology.bsdiff.BSPatch.patchFast;

public class BSUtil {

    // JBDiff extensions by Stefan.Liebig@compeople.de:
    //
    // - introduced a HEADER_SIZE constant here

    /**
     * Length of the diff file header.
     */
    public static final int HEADER_SIZE = 32;
    public static final int BUFFER_SIZE = 8192;


    /**
     * Read from input stream and fill the given buffer from the given offset up
     * to length len.
     */
    public static final boolean readFromStream(InputStream in, byte[] buf, int offset, int len) throws IOException {

        int totalBytesRead = 0;
        while (totalBytesRead < len) {
            int bytesRead = in.read(buf, offset + totalBytesRead, len - totalBytesRead);
            if (bytesRead < 0) {
                return false;
            }
            totalBytesRead += bytesRead;
        }
        return true;
    }

    /**
     * input stream to byte
     *
     * @param in InputStream
     * @return byte[]
     * @throws IOException
     */
    public static byte[] inputStreamToByte(InputStream in) throws IOException {

        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] data = new byte[BUFFER_SIZE];
        int count = -1;
        while ((count = in.read(data, 0, BUFFER_SIZE)) != -1) {
            outStream.write(data, 0, count);
        }

        data = null;
        return outStream.toByteArray();
    }

    public static void main(String[] arg) throws Exception {
        if (4 <= arg.length) {
            String exe = arg[0];
            if ("diff".equals(exe)) {
                File oldFile = new File(arg[1]);
                File newFile = new File(arg[2]);
                File diffFile = new File(arg[3]);

                bsdiff(oldFile, newFile, diffFile);
                return;
            } else if ("patch".equals(exe)) {
                File oldFile = new File(arg[1]);
                File diffFile = new File(arg[2]);
                File newFile = new File(arg[3]);

                patchFast(oldFile, newFile, diffFile, 2);
                return;
            }
        }
        System.out.println("BsDiff help");
        System.out.println("\tdiff <old file> <new file> <out diff file>");
        System.out.println("\tpatch <old file> <diff file> <out file>");
        System.out.println("");
    }
}
