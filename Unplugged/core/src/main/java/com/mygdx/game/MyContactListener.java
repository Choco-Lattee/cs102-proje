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
    private boolean contactWithWater;
    private boolean contactWithLava;
    private Fixture lastRobot1, lastRobot2;
    private boolean[] areDoorsOpen = {false, false, false, false, false ,false};
    private boolean[] contactWithDoors = {false, false, false, false, false ,false};
    private boolean[] npcRescued = {false, false, false};

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
                lastRobot1 = fa;
                lastRobot2 = fb;
            }
            if (fb.getUserData().equals("foot") && fa.getUserData().equals("robot")) {
                isContactWithRobot = true;
                lastRobot1 = fa;
                lastRobot2 = fb;
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
            if (fa.getUserData().equals("foot") && fb.getUserData().equals("water")) {
                contactWithWater = true;
                System.out.println("water");
            }
            if (fb.getUserData().equals("foot") && fa.getUserData().equals("water")) {
                contactWithWater = true;
                System.out.println("water");
            }
            if (fa.getUserData().equals("foot") && fb.getUserData().equals("lava")) {
                contactWithLava = true;
                System.out.println("lava");
            }
            if (fb.getUserData().equals("foot") && fa.getUserData().equals("lava")) {
                contactWithLava = true;
                System.out.println("lava");
            }

            if (fa.getUserData().equals("stealthNpc1") && fb.getUserData().equals("player")) {
                npcRescued[0] = true;
            }
            if (fb.getUserData().equals("stealthNpc1") && fa.getUserData().equals("player")) {
                npcRescued[0] = true;
            }

            if (fa.getUserData().equals("stealthNpc2") && fb.getUserData().equals("player")) {
                npcRescued[1] = true;
            }
            if (fb.getUserData().equals("stealthNpc2") && fa.getUserData().equals("player")) {
                npcRescued[1] = true;
            }

            if (fa.getUserData().equals("stealthNpc3") && fb.getUserData().equals("player")) {
                npcRescued[2] = true;
            }
            if (fb.getUserData().equals("stealthNpc3") && fa.getUserData().equals("player")) {
                npcRescued[2] = true;
            }

            try{
                Integer.parseInt("" + fb.getUserData());
                int temp = (int) fb.getUserData();

                if (fa.getUserData().equals("player") && temp < 7 && !contactWithDoors[temp - 1]) {
                    contactWithDoors[temp - 1] = true;
                }
                if (fa.getUserData().equals("player") && temp > 9 && !areDoorsOpen[temp / 10 - 1]) {
                    areDoorsOpen[temp / 10 - 1] = true;
                }
            }
            catch(NumberFormatException e){}

            try{
                Integer.parseInt("" + fa.getUserData());
                int temp = (int) fa.getUserData();

                if (fb.getUserData().equals("player") && temp < 7 && !contactWithDoors[temp - 1]) {
                    contactWithDoors[temp - 1] = true;
                }
                if (fb.getUserData().equals("player") && temp > 9 && !areDoorsOpen[temp / 10 - 1]) {
                    areDoorsOpen[temp / 10 - 1] = true;
                }
            }
            catch(NumberFormatException e){}
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
            if (fa.getUserData().equals("foot") && fb.getUserData().equals("water")) {
                contactWithWater = false;
                System.out.println("water");
            }
            if (fb.getUserData().equals("foot") && fa.getUserData().equals("water")) {
                contactWithWater = false;
                System.out.println("water");
            }
            if (fa.getUserData().equals("foot") && fb.getUserData().equals("lava")) {
                contactWithLava = false;
            }
            if (fb.getUserData().equals("foot") && fa.getUserData().equals("lava")) {
                contactWithLava = false;
            }

            try{
                Integer.parseInt("" + fb.getUserData());
                int temp = (int) fb.getUserData();

                if (fa.getUserData().equals("player") && temp < 7 && contactWithDoors[temp - 1]) {
                    contactWithDoors[temp - 1] = false;
                }
            }
            catch(NumberFormatException e){}

            try{
                Integer.parseInt("" + fa.getUserData());
                int temp = (int) fa.getUserData();

                if (fb.getUserData().equals("player") && temp < 7 && areDoorsOpen[temp / 10 - 1]) {
                    contactWithDoors[temp - 1] = false;
                }
            }
            catch(NumberFormatException e){}
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
    public boolean onContactWithWater() {
        return contactWithWater;
    }
    public boolean onContactWithLava() {
        return contactWithLava;
    }

    public Fixture getLastPos1Robot() {
        return lastRobot1;
    }

    public Fixture getLastPos2Robot() {
        return lastRobot2;
    }

        public boolean[] getAreDoorsOpen() {
        return areDoorsOpen;
    }
    public boolean[] getContactWithDoors() {
        return contactWithDoors;
    }

    public boolean[] getNpcRescued() {
        return npcRescued;
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {}

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {}
    
}
