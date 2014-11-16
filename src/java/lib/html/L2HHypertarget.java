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
import java.io.EOFException;

import com.dickimawbooks.texparserlib.*;
import com.dickimawbooks.texparserlib.latex.*;

public class L2HHypertarget extends Command
{
   public L2HHypertarget()
   {
      this("hypertarget");
   }

   public L2HHypertarget(String name)
   {
      super(name);
   }

   public Object clone()
   {
      return new L2HHypertarget(getName());
   }

   public TeXObjectList expandonce(TeXParser parser)
      throws IOException
   {
      TeXObject target = parser.popNextArg();

      if (target instanceof Expandable)
      {
         TeXObjectList expanded = ((Expandable)target).expandfully(parser);

         if (expanded != null)
         {
            target = expanded;
         }
      }

      TeXObject text = parser.popNextArg();

      if (text instanceof Expandable)
      {
         TeXObjectList expanded = ((Expandable)text).expandonce(parser);

         if (expanded != null)
         {
            text = expanded;
         }
      }


      TeXObjectList list = new TeXObjectList();
      list.add(new HtmlTag("<a name=\""+
        HtmlTag.getUriFragment(target.toString(parser))+"\">"));
      list.add(text);
      list.add(new HtmlTag("</a>"));

      return list;
   }

   public TeXObjectList expandonce(TeXParser parser, TeXObjectList stack)
      throws IOException
   {
      TeXObject target = stack.popArg();

      if (target instanceof Expandable)
      {
         TeXObjectList expanded = ((Expandable)target).expandfully(parser, stack);

         if (expanded != null)
         {
            target = expanded;
         }
      }

      TeXObject text = stack.popArg();

      if (text instanceof Expandable)
      {
         TeXObjectList expanded = ((Expandable)text).expandonce(parser, stack);

         if (expanded != null)
         {
            text = expanded;
         }
      }

      TeXObjectList list = new TeXObjectList();
      list.add(new HtmlTag("<a name=\""+
        HtmlTag.getUriFragment(target.toString(parser))+"\">"));
      list.add(text);
      list.add(new HtmlTag("</a>"));

      return list;
   }


}
