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

public class L2HSection extends Section
{
   public L2HSection()
   {
      this("section");
   }

   public L2HSection(String name)
   {
      super(name);
   }

   public Object clone()
   {
      return new L2HSection(getName());
   }

   protected void unnumbered(TeXParser parser, TeXObjectList stack,
       TeXObject arg)
   throws IOException
   {
      L2HConverter listener = (L2HConverter)parser.getListener();

      String tag = getTag();

      if (tag == null)
      {
         listener.write("<div class=\""+getName()+"\">");
      }
      else
      {
         listener.write("<"+tag+">");
      }

      TeXObject cs = parser.getControlSequence("theH"+getName()+"*");

      if (cs == null)
      {
         cs = parser.getControlSequence("the"+getName()+"*");
      }

      if (cs != null)
      {
         if (cs instanceof Expandable)
         {
            TeXObjectList expanded;

            if (parser == stack || stack == null)
            {
               expanded = ((Expandable)cs).expandfully(parser);
            }
            else
            {
               expanded = ((Expandable)cs).expandfully(parser, stack);
            }

            if (expanded != null)
            {
               cs = expanded;
            }
         }

         String counter = getName()+"*";

         listener.write(String.format("<a name=\"%s\"></a>",
           HtmlTag.getUriFragment(counter+"."+cs.toString(parser))));

         listener.stepcounter(counter);
      }

      if (parser == stack || stack == null)
      {
         arg.process(parser);
      }
      else
      {
         arg.process(parser, stack);
      }

      if (tag == null)
      {
         listener.write("</div>");
      }
      else
      {
         listener.write("</"+tag+">");
      }
   }

   protected void numbered(TeXParser parser, TeXObjectList stack,
     TeXObject optArg, TeXObject arg)
       throws IOException
   {
      L2HConverter listener = (L2HConverter)parser.getListener();

      TeXObjectList list = new TeXObjectList();

      String tag = getTag();

      if (tag == null)
      {
         list.add(new HtmlTag("<div class=\""+getName()+"\">"));
      }
      else
      {
         list.add(new HtmlTag("<"+tag+">"));
      }

      list.add(listener.getControlSequence("the"+getName()));
      list.add(listener.getOther('.'));
      list.add(listener.getSpace());

      // Is there a label following the section command?

      TeXObject object;

      if (parser == stack || stack == null)
      {
         object = parser.popStack();
      }
      else
      {
         object = stack.popStack();
      }

      while (object instanceof WhiteSpace || object.isPar())
      {
         if (parser == stack || stack == null)
         {
            object = parser.popStack();
         }
         else
         {
            object = stack.popStack();
         }
      }

      if (object instanceof TeXCsRef)
      {
         object = listener.getControlSequence(((TeXCsRef)object).getName());
      }

      if (object instanceof Label)
      {
         TeXObject label;

         if (parser == stack || stack == null)
         {
            label = parser.popNextArg();
         }
         else
         {
            label = stack.popArg();
         }

         if (label instanceof Expandable)
         {
            TeXObjectList expanded;

            if (parser == stack || stack == null)
            {
               expanded = ((Expandable)label).expandfully(parser);
            }
            else
            {
               expanded = ((Expandable)label).expandfully(parser, stack);
            }

            if (expanded != null)
            {
               label = expanded;
            }
         }

         list.add(1, new HtmlTag("<a name=\""
           +HtmlTag.getUriFragment(label.toString(parser))+"\">"));
         list.add(new HtmlTag("</a>"));
      }
      else
      {
         if (parser == stack || stack == null)
         {
            parser.push(object);
         }
         else
         {
            stack.push(object);
         }

         TeXObject cs = parser.getControlSequence("theH"+getName());

         if (cs == null)
         {
            cs = parser.getControlSequence("the"+getName());
         }

         if (cs != null)
         {
            if (cs instanceof Expandable)
            {
               TeXObjectList expanded;

               if (parser == stack || stack == null)
               {
                  expanded = ((Expandable)cs).expandfully(parser);
               }
               else
               {
                  expanded = ((Expandable)cs).expandfully(parser, stack);
               }

               if (expanded != null)
               {
                  cs = expanded;
               }
            }

            list.add(1, new HtmlTag("<a name=\""
              +HtmlTag.getUriFragment(getName()+"."+cs.toString(parser))+"\">"));
            list.add(new HtmlTag("</a>"));
         }
      }

      list.add(arg);

      if (tag == null)
      {
         list.add(new HtmlTag("</div>"));
      }
      else
      {
         list.add(new HtmlTag("</"+tag+">"));
      }

      if (parser == stack || stack == null)
      {
         if (!parser.isPar(parser.peekStack()))
         {
            list.add(listener.getPar());
         }

         list.process(parser);
      }
      else
      {
         if (!parser.isPar(stack.peekStack()))
         {
            list.add(listener.getPar());
         }

         list.process(parser, stack);
      }
   }

   public String getTag()
   {
      String name = getName();

      for (int i = 0; i < TAGS.length; i++)
      {
         if (TAGS[i][0].equals(name))
         {
            return TAGS[i][1];
         }
      }

      return null;
   }

   private static final String[][] TAGS = new String[][]
   {
      new String[] {"chapter", "h1"},
      new String[] {"section", "h2"},
      new String[] {"subsection", "h3"},
      new String[] {"subsubsection", "h4"},
      new String[] {"paragraph", "h5"}
   };
}
