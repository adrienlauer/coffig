/**
 * Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.coffig.data.mutable;

import org.seedstack.coffig.PropertyNotFoundException;
import org.seedstack.coffig.data.MapNode;
import org.seedstack.coffig.data.NamedNode;
import org.seedstack.coffig.data.TreeNode;

import java.util.HashMap;
import java.util.Map;

import static org.seedstack.coffig.data.mutable.MutableTreeNodeFactory.createFromPrefix;

public class MutableMapNode extends MapNode implements MutableTreeNode {

    public MutableMapNode(NamedNode... childNodes) {
        super(childNodes);
    }

    public MutableMapNode(Map<String, TreeNode> newChildNodes) {
        super(newChildNodes);
    }

    public MutableMapNode() {
        super(new HashMap<>());
    }

    public TreeNode put(String key, TreeNode value) {
        return childNodes.put(key, value);
    }

    public void putAll(Map<? extends String, ? extends TreeNode> m) {
        childNodes.putAll(m);
    }

    public void clear() {
        childNodes.clear();
    }

    @Override
    public MutableTreeNode set(String name, TreeNode value) {
        Prefix prefix = new Prefix(name);
        if (prefix.tail.isPresent()) {
            MutableTreeNode nexNode = getOrCreateNode(prefix);
            MutableTreeNode finalNode = nexNode.set(prefix.tail.get(), value);
            childNodes.put(prefix.head, nexNode);
            return finalNode;
        } else {
            childNodes.put(prefix.head, value);
            return (MutableTreeNode) value;
        }
    }

    private MutableTreeNode getOrCreateNode(Prefix prefix) {
        MutableTreeNode mutableTreeNode;
        if (childNodes.containsKey(prefix.head)) {
            TreeNode treeNode = childNodes.get(prefix.head);
            assertMutable(treeNode);
            mutableTreeNode = (MutableTreeNode) treeNode;
        } else {
            mutableTreeNode = createFromPrefix(prefix.tail.get());
        }
        return mutableTreeNode;
    }

    @Override
    public MutableTreeNode remove(String name) {
        Prefix prefix = new Prefix(name);
        if (prefix.tail.isPresent()) {
            if (childNodes.containsKey(prefix.head)) {
                TreeNode treeNode = childNodes.get(prefix.head);
                assertMutable(treeNode);
                MutableTreeNode mutableTreeNode = (MutableTreeNode) treeNode;
                MutableTreeNode removedNode = mutableTreeNode.remove(prefix.tail.get());
                removeEmptyIntermediateNode(prefix, mutableTreeNode);
                return removedNode;
            } else {
                throw new PropertyNotFoundException(name);
            }
        } else {
            return (MutableTreeNode) childNodes.remove(prefix.head);
        }
    }

    private void removeEmptyIntermediateNode(Prefix prefix, MutableTreeNode mutableTreeNode) {
        if (mutableTreeNode.isEmpty()) {
            childNodes.remove(prefix.head);
        }
    }

    @Override
    public boolean isEmpty() {
        return childNodes.isEmpty();
    }
}
