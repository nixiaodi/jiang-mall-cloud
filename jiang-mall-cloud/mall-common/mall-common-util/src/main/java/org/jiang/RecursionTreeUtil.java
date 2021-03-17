package org.jiang;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RecursionTreeUtil {

    /**
     * get child tree nodes
     */
    public static List<TreeNode> getChildrenTreeNodes(List<TreeNode> candidateNodeList,Long parentId) {
        List<TreeNode> resultList = new ArrayList<>();

        for (TreeNode treeNode : candidateNodeList) {
            if (treeNode.getPid() == null) {
                continue;
            }

            if (Objects.equals(treeNode.getPid(),parentId)) {
                recursionFn(candidateNodeList,treeNode);
                resultList.add(treeNode);
            }
        }
        return resultList;
    }

    /**
     * 递归列表
     */
    private static void recursionFn(List<TreeNode> candidateNodeList,TreeNode node) {
        List<TreeNode> childList = getChildList(candidateNodeList, node);
        if (PublicUtil.isEmpty(childList)) {
            return;
        }
        node.setChildren(childList);
        for (TreeNode treeNode : childList) {
            recursionFn(candidateNodeList,treeNode);
        }
    }

    /**
     * get child node list
     */
    private static List<TreeNode> getChildList(List<TreeNode> candidateNodeList,TreeNode node) {
        List<TreeNode> resultList = new ArrayList<>();

        for (TreeNode treeNode : candidateNodeList) {
            if (PublicUtil.isEmpty(treeNode.getPid())) {
                continue;
            }
            if (Objects.equals(treeNode.getPid(),node.getId())) {
                resultList.add(treeNode);
            }
        }

        return resultList;
    }
}
