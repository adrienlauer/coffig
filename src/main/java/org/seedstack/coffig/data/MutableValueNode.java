/**
 * Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.coffig.data;

public class MutableValueNode extends ValueNode implements MutableTreeNode {
    public MutableValueNode(String value) {
        super(value);
    }

    public MutableValueNode() {
        super(null);
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public MutableTreeNode set(String prefix, TreeNode value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MutableTreeNode remove(String prefix) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public TreeNode freeze() {
        return new ValueNode(value);
    }

    @Override
    public MutableTreeNode unfreeze() {
        return this;
    }
}