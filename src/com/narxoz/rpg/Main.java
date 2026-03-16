package com.narxoz.rpg;

import com.narxoz.rpg.battle.RaidEngine;
import com.narxoz.rpg.battle.RaidResult;
import com.narxoz.rpg.bridge.AreaSkill;
import com.narxoz.rpg.bridge.FireEffect;
import com.narxoz.rpg.bridge.IceEffect;
import com.narxoz.rpg.bridge.PhysicalEffect;
import com.narxoz.rpg.bridge.ShadowEffect;
import com.narxoz.rpg.bridge.SingleTargetSkill;
import com.narxoz.rpg.bridge.Skill;
import com.narxoz.rpg.composite.EnemyUnit;
import com.narxoz.rpg.composite.HeroUnit;
import com.narxoz.rpg.composite.PartyComposite;
import com.narxoz.rpg.composite.RaidGroup;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Homework 4 Demo: Bridge + Composite ===\n");

        HeroUnit knight = new HeroUnit("Knight", 140, 30);
        HeroUnit mage = new HeroUnit("Mage", 90, 40);
        HeroUnit archer = new HeroUnit("Archer", 100, 28);
        HeroUnit priest = new HeroUnit("Priest", 85, 22);

        EnemyUnit goblin = new EnemyUnit("Goblin", 70, 20);
        EnemyUnit orc = new EnemyUnit("Orc", 120, 25);
        EnemyUnit assassin = new EnemyUnit("Assassin", 80, 30);
        EnemyUnit necromancer = new EnemyUnit("Necromancer", 95, 24);

        PartyComposite vanguard = new PartyComposite("Vanguard");
        vanguard.add(knight);
        vanguard.add(mage);

        PartyComposite support = new PartyComposite("Support");
        support.add(archer);
        support.add(priest);

        RaidGroup heroes = new RaidGroup("Alliance Raid");
        heroes.add(vanguard);
        heroes.add(support);

        PartyComposite frontline = new PartyComposite("Frontline");
        frontline.add(goblin);
        frontline.add(orc);

        PartyComposite shadowSquad = new PartyComposite("Shadow Squad");
        shadowSquad.add(assassin);
        shadowSquad.add(necromancer);

        RaidGroup darkWing = new RaidGroup("Dark Wing");
        darkWing.add(shadowSquad);

        RaidGroup enemies = new RaidGroup("Enemy Raid");
        enemies.add(frontline);
        enemies.add(darkWing);

        System.out.println("--- Composite Hierarchy Demo ---");
        System.out.println("Alliance structure:");
        heroes.printTree("");
        System.out.println();
        System.out.println("Enemy structure:");
        enemies.printTree("");

        Skill slashFire = new SingleTargetSkill("Slash", 20, new FireEffect());
        Skill slashIce = new SingleTargetSkill("Slash", 20, new IceEffect());
        Skill stormFire = new AreaSkill("Storm", 15, new FireEffect());
        Skill blastShadow = new AreaSkill("Blast", 18, new ShadowEffect());
        Skill strikePhysical = new SingleTargetSkill("Strike", 22, new PhysicalEffect());

        System.out.println("\n--- Bridge Demo ---");
        System.out.println("Same skill with different effects:");
        System.out.println("1) " + slashFire.getSkillName() + " uses " + slashFire.getEffectName());
        System.out.println("2) " + slashIce.getSkillName() + " uses " + slashIce.getEffectName());

        System.out.println("\nSame effect with different skills:");
        System.out.println("1) " + slashFire.getSkillName() + " uses " + slashFire.getEffectName());
        System.out.println("2) " + stormFire.getSkillName() + " uses " + stormFire.getEffectName());

        System.out.println("\nAdditional skill combinations:");
        System.out.println("- " + blastShadow.getSkillName() + " uses " + blastShadow.getEffectName());
        System.out.println("- " + strikePhysical.getSkillName() + " uses " + strikePhysical.getEffectName());

        RaidEngine engine = new RaidEngine().setRandomSeed(42L);
        RaidResult result = engine.runRaid(heroes, enemies, slashFire, stormFire);

        System.out.println("\n--- Raid Simulation ---");
        System.out.println("Team A skill: " + slashFire.getSkillName() + " [" + slashFire.getEffectName() + "]");
        System.out.println("Team B skill: " + stormFire.getSkillName() + " [" + stormFire.getEffectName() + "]");
        System.out.println("Winner: " + result.getWinner());
        System.out.println("Rounds: " + result.getRounds());

        System.out.println("\nBattle log:");
        for (String line : result.getLog()) {
            System.out.println(line);
        }

        System.out.println("\n=== Demo Complete ===");
    }
}