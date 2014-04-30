/*
 * gnu.java.beans.editors.NativeIntEditor: part of the Java Class Libraries project.
 * Copyright (C) 1998 John Keiser
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Library General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA  02111-1307, USA.
 */

package gnu.java.beans.editors;

import java.beans.*;

/**
 ** NativeIntEditor is a property editor for the
 ** int type.
 **
 ** @author John Keiser
 ** @version 1.1.0, 29 Jul 1998
 **/

public class NativeIntEditor extends PropertyEditorSupport {
	/** setAsText for int calls Integer.valueOf(). **/
	public void setAsText(String val) throws IllegalArgumentException {
		setValue(Integer.valueOf(val));
	}

	/** getAsText for int calls Integer.toString(). **/
	public String getAsText() {
		return getValue().toString();
	}
}
