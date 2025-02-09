package com.guhao.star.efmex;

import yesman.epicfight.api.collider.Collider;
import yesman.epicfight.api.collider.MultiOBBCollider;

public class StarColliderPreset {
    public static final Collider EXECUTE = new MultiOBBCollider(2, 0.8, 0.8, 1.383, 0.0, 0.0, -0.66);
    public static final Collider KILL = new MultiOBBCollider(1, 0, 0.8, 1.383, 0.8, 0.8, 0.8);
    public static final Collider EXECUTED = new MultiOBBCollider(1, 0, 0, 0, 0, 0, 0);
}
