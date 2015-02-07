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
package com.dickimawbooks.texparserlib.html;

import java.io.IOException;
import java.util.Vector;

import com.dickimawbooks.texparserlib.*;

public class L2HMathGroup extends MathGroup
{
   public L2HMathGroup()
   {
      super();
   }

   public L2HMathGroup(int capacity)
   {
      super(capacity);
   }

   public TeXObjectList createList()
   {
      L2HMathGroup math = new L2HMathGroup(capacity());
      math.setInLine(isInLine());
      return math;
   }

   public void processList(TeXParser parser, TeXObjectList stack)
    throws IOException
   {
      L2HConverter listener = (L2HConverter)parser.getListener();

      while (size() > 0)
      {
         TeXObject object = expandedPopStack(parser);

         if (listener.useMathJax() && object instanceof ControlSequence)
         {
            listener.write(object.toString());

            if (((ControlSequence)object).isControlWord(parser))
            {
               listener.write(' ');
            }
         }
         else if (stack != parser && size() == 0)
         {
            object.process(parser, stack);
         }
         else
         {
            object.process(parser, this);
         }
      }
   }

   public void process(TeXParser parser) throws IOException
   {
      process(parser, parser);
   }

   public void process(TeXParser parser, TeXObjectList list) throws IOException
   {
      L2HConverter listener = (L2HConverter)parser.getListener();

      parser.startGroup();

      int orgMode = parser.getSettings().getCurrentMode();

      if (!isInLine())
      {
         parser.getSettings().setMode(TeXSettings.MODE_DISPLAY_MATH);

         listener.write("<div class=\"displaymath\">");
      }
      else
      {
         parser.getSettings().setMode(TeXSettings.MODE_INLINE_MATH);
      }

      if (listener.useMathJax())
      {
         if (isInLine())
         {
            listener.write(listener.mathJaxStartInline());
            processList(parser, list);
            listener.write(listener.mathJaxEndInline());
         }
         else
         {
            listener.write(listener.mathJaxStartDisplay());
            processList(parser, list);
            listener.write(listener.mathJaxEndDisplay());
         }
      }
      else
      {
         processList(parser, list);
      }

      if (!isInLine())
      {
         listener.write("</div>");
      }

      parser.getSettings().setMode(orgMode);

      parser.endGroup();
   }

}
