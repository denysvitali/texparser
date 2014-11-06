/*
    Copyright (C) 2013 Nicola L.C. Talbot
    www.dickimaw-books.com

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
*/
package com.dickimawbooks.texparserlib.latex;

import java.io.IOException;
import java.io.EOFException;
import java.io.File;
import java.io.PrintWriter;
import java.util.Hashtable;
import java.util.Vector;
import java.nio.file.Path;
import java.nio.file.Files;

import com.dickimawbooks.texparserlib.*;
import com.dickimawbooks.texparserlib.generic.*;

/**
 * Parses LaTeX document preamble
 */

public class PreambleParser extends LaTeXParserListener
  implements Writeable
{
   public PreambleParser(TeXApp texApp)
     throws IOException
   {
      super(null);
      this.texApp = texApp;

      setWriteable(this);

   }

   public TeXApp getTeXApp()
   {
      return texApp;
   }

   public TeXParser parse(File texFile)
     throws IOException
   {
      TeXParser parser = new TeXParser(this);

      parser.parse(texFile);

      return parser;
   }

   public Writeable getWriteable()
   {
      return this;
   }

   public void write(String text)
     throws IOException
   {
   }

   public void writeln(String text)
     throws IOException
   {
   }

   public void write(char c)
     throws IOException
   {
   }

   public void writeCodePoint(int codePoint)
     throws IOException
   {
   }

   public void overwithdelims(TeXParser parser, TeXObject firstDelim,
     TeXObject secondDelim, TeXObject before, TeXObject after)
    throws IOException
   {
   }

   public void abovewithdelims(TeXParser parser, TeXObject firstDelim,
     TeXObject secondDelim, TeXDimension thickness, TeXObject before, 
     TeXObject after)
    throws IOException
   {
   }

   public void par() throws IOException
   {
   }

   public void input(TeXParser parser, TeXPath path)
    throws IOException
   {
   }

   public void tab(TeXParser parser)
     throws IOException
   {
   }

   public void skipping(TeXParser parser, Ignoreable ignoreable)
      throws IOException
   {
   }

   public void substituting(TeXParser parser, String original, String replacement)
   {
      texApp.substituting(parser.getLineNumber(), original, replacement);
   }

   public void href(TeXParser parser, String url, TeXObject text)
      throws IOException
   {
   }

   public void includegraphics(TeXParser parser, 
     KeyValList options, String imgName)
     throws IOException
   {
   }

   public void subscript(TeXParser parser, TeXObject arg)
     throws IOException
   {
   }

   public void superscript(TeXParser parser, TeXObject arg)
     throws IOException
   {
   }

   public void endParse(TeXParser parser, File file)
      throws IOException
   {
      this.file = null;
   }

   public void beginParse(TeXParser parser, File file)
      throws IOException
   {
      this.file = file;
   }

   public File getFile()
   {
      return file;
   }

   public void beginDocument(TeXParser parser)
     throws IOException
   {
      super.beginDocument(parser);
      endDocument(parser);
   }

   private TeXApp texApp;

   private File file;
}