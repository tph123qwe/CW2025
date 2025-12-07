package com.comp2042.logic.bricks;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

public class RandomBrickGenerator implements BrickGenerator {

    private final List<Brick> brickList;
    private final Deque<Brick> nextBricks = new ArrayDeque<>();
    private final List<Brick> currentBag = new ArrayList<>();

    public RandomBrickGenerator() {
        brickList = new ArrayList<>();
        brickList.add(new IBrick());
        brickList.add(new JBrick());
        brickList.add(new LBrick());
        brickList.add(new OBrick());
        brickList.add(new SBrick());
        brickList.add(new TBrick());
        brickList.add(new ZBrick());

        fillBag();

        while (nextBricks.size() < 3) {
            nextBricks.add(getNextBrickFromBag());
        }
    }

    private void fillBag() {
        currentBag.clear();
        currentBag.addAll(brickList);
        Collections.shuffle(currentBag);
    }

    private Brick getNextBrickFromBag() {
        if (currentBag.isEmpty()) {
            fillBag();
        }
        return currentBag.remove(0);
    }

    @Override
    public Brick getBrick() {
        if (nextBricks.size() <= 2) {
            nextBricks.add(getNextBrickFromBag());
        }
        return nextBricks.poll();
    }

    @Override
    public Brick getNextBrick() {
        return nextBricks.peek();
    }

    public List<int[][]> getNextBricksMatrices() {
        List<int[][]> list = new ArrayList<>();
        int count = 0;
        for (Brick b : nextBricks) {
            if (count >= 3) break;
            list.add(b.getShapeMatrix().get(0));
            count++;
        }
        while (list.size() < 3) list.add(null);
        return list;
    }
}