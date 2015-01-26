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
package com.dickimawbooks.texparserlib.latex2latex;

import java.io.IOException;
import java.io.Writer;

import com.dickimawbooks.texparserlib.*;
import com.dickimawbooks.texparserlib.primitives.*;
import com.dickimawbooks.texparserlib.latex.*;

public class L2LGroup extends Group
{
   public L2LGroup()
   {
      super();
   }

   public L2LGroup(int capacity)
   {
      super(capacity);
   }

   public L2LGroup(TeXParserListener listener, String text)
   {
      super(listener, text);
   }

   public Object clone()
   {
      L2LGroup group = new L2LGroup();

      group.addAll(this);

      return group;
   }

   private void preprocess(TeXParser parser)
      throws IOException
   {
      if (size() == 0)
      {
         return;
      }

      LaTeX2LaTeX listener = (LaTeX2LaTeX)parser.getListener();

      Writeable writeable = listener.getWriteable();

      TeXObject object = pop();

      if (parser.isMathMode() && object instanceof Obsolete)
      {
         Obsolete obs = (Obsolete)object;

         ControlSequence original = 
            ((Obsolete)object).getOriginalCommand();

         if (original instanceof TeXFontDeclaration)
         {
            ControlSequence cs = listener.getControlSequence(
               "math"+original.getName());

            String replacement = cs.toString(parser);

            listener.substituting(
               original.toString(parser), 
               replacement);

            writeable.write(replacement);

            process(parser);

            return;
         }
      }

      push(object);
   }

   public void process(TeXParser parser, TeXObjectList stack)
      throws IOException
   {
      preprocess(parser);

      LaTeX2LaTeX listener = (LaTeX2LaTeX)parser.getListener();

      Writeable writeable = listener.getWriteable();
      writeable.write(parser.getBgChar());

      parser.startGroup();

      processList(parser, stack);

      parser.endGroup();

      writeable.write(parser.getEgChar());
   }


}
