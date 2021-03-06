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
package com.dickimawbooks.texparserlib.generic;

import java.io.IOException;
import java.util.Vector;

import com.dickimawbooks.texparserlib.*;

public class DoubleLetterAccent extends ControlSequence
{
   public DoubleLetterAccent(String name, int combiningCodePt)
   {
      this(name, combiningCodePt, -1);
   }

   public DoubleLetterAccent(String name, int combiningCodePt, int noCharCodePt)
   {
      super(name);
      this.combiningCodePt = combiningCodePt;
      this.noCharCodePt = noCharCodePt;
   }

   public Object clone()
   {
      return new DoubleLetterAccent(getName(), combiningCodePt, noCharCodePt);
   }

   public void process(TeXParser parser) throws IOException
   {
      TeXObject arg = parser.popNextArg();

      if (!(arg instanceof TeXObjectList))
      {
         parser.push(parser.getListener().getOther(combiningCodePt));
         parser.push(parser.getListener().getOther(0x00A0));
         arg.process(parser);
         return;
      }

      TeXObjectList list = (TeXObjectList)arg;

      if (list.isEmpty())
      {
         if (noCharCodePt == -1)
         {
            parser.push(parser.getListener().getOther(0x00A0));
            parser.push(parser.getListener().getOther(combiningCodePt));
            parser.push(parser.getListener().getOther(0x00A0));
         }
         else
         {
            parser.push(parser.getListener().getOther(noCharCodePt));
         }

         return;
      }

      list.add(1, parser.getListener().getOther(combiningCodePt));

      list.process(parser);
   }

   public void process(TeXParser parser, TeXObjectList stack) throws IOException
   {
      TeXObject arg = stack.popArg(parser);

      if (!(arg instanceof TeXObjectList))
      {
         stack.push(parser.getListener().getOther(combiningCodePt));
         stack.push(parser.getListener().getOther(0x00A0));
         arg.process(parser, stack);
         return;
      }

      TeXObjectList list = (TeXObjectList)arg;

      if (list.isEmpty())
      {
         if (noCharCodePt == -1)
         {
            stack.push(parser.getListener().getOther(0x00A0));
            stack.push(parser.getListener().getOther(combiningCodePt));
            stack.push(parser.getListener().getOther(0x00A0));
         }
         else
         {
            stack.push(parser.getListener().getOther(noCharCodePt));
         }

         return;
      }

      list.add(1, parser.getListener().getOther(combiningCodePt));

      list.process(parser, stack);
   }

   private int combiningCodePt, noCharCodePt;
}
