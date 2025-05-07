package com.mygdx.game;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

public class MyContactListener implements ContactListener{
    private int playerIsOnGround;
    private boolean isContactWithRobot;
    private boolean contactWithNPC;
    private boolean contactWithFireball;

    @Override
    public void beginContact(Contact contact) {
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();

        if (fa.getUserData() != null && fa.getUserData().equals("foot")) {
            playerIsOnGround++;
        }
        if (fb.getUserData() != null && fb.getUserData().equals("foot")) {
            playerIsOnGround++;
        }
        // contacts with robots
        if (fa.getUserData() != null && fb.getUserData() != null) {
            if (fa.getUserData().equals("foot") && fb.getUserData().equals("robot")) {
                isContactWithRobot = true;
            }
            if (fb.getUserData().equals("foot") && fa.getUserData().equals("robot")) {
                isContactWithRobot = true;
            }
            if (fa.getUserData().equals("foot") && fb.getUserData().equals("npc")) {
                contactWithNPC = true;
                System.out.println("NPC");
            }
            if (fb.getUserData().equals("foot") && fa.getUserData().equals("npc")) {
                contactWithNPC = true;
                System.out.println("NPC");
            }
            if (fa.getUserData().equals("foot") && fb.getUserData().equals("fireball")) {
                contactWithFireball = true;
                System.out.println("fireeee");
            }
            if (fb.getUserData().equals("foot") && fa.getUserData().equals("fireball")) {
                contactWithFireball = true;
                System.out.println("fireeee");
            }
        }
    }

    @Override
    public void endContact(Contact contact) {
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();

        if (fa.getUserData() != null && fa.getUserData().equals("foot")) {
            playerIsOnGround--;
        }
        if (fb.getUserData() != null && fb.getUserData().equals("foot")) {
            playerIsOnGround--;
        }
        if (fa.getUserData() != null && fb.getUserData() != null) {
            if (fa.getUserData().equals("foot") && fb.getUserData().equals("robot")) {
                isContactWithRobot = false;
            }
            if (fb.getUserData().equals("foot") && fa.getUserData().equals("robot")) {
                isContactWithRobot = false;
            }
            if (fa.getUserData().equals("foot") && fb.getUserData().equals("npc")) {
                contactWithNPC = false;
            }
            if (fb.getUserData().equals("foot") && fa.getUserData().equals("npc")) {
                contactWithNPC = false;
            }
            if (fa.getUserData().equals("foot") && fb.getUserData().equals("fireball")) {
                contactWithFireball = false;
            }
            if (fb.getUserData().equals("foot") && fa.getUserData().equals("fireball")) {
                contactWithFireball = false;
            }

        }
    }

    public boolean isPlayerOnGround() {
        return (playerIsOnGround > 0);
    }
    public boolean isOnContactWithNPC() {
        return contactWithNPC;
    }
    public boolean onContactWithRobotAndPlayer() {
        return isContactWithRobot;
    }
    public boolean onContactWithFireball() {
        return contactWithFireball;
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {}

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {}
    
}
