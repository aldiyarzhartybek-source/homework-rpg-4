package com.narxoz.rpg.battle;

import com.narxoz.rpg.bridge.AreaSkill;
import com.narxoz.rpg.bridge.Skill;
import com.narxoz.rpg.composite.CombatNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RaidEngine {
    private static final int MAX_ROUNDS = 100;
    private Random random = new Random(1L);

    public RaidEngine setRandomSeed(long seed) {
        this.random = new Random(seed);
        return this;
    }

    public RaidResult runRaid(CombatNode teamA, CombatNode teamB, Skill teamASkill, Skill teamBSkill) {
        if (teamA == null || teamB == null || teamASkill == null || teamBSkill == null) {
            throw new IllegalArgumentException("Teams and skills must not be null.");
        }

        RaidResult result = new RaidResult();

        if (!teamA.isAlive() && !teamB.isAlive()) {
            result.setRounds(0);
            result.setWinner("Draw");
            result.addLine("Both teams are already defeated.");
            return result;
        }

        if (!teamA.isAlive()) {
            result.setRounds(0);
            result.setWinner(teamB.getName());
            result.addLine(teamA.getName() + " is already defeated.");
            result.addLine("Winner: " + teamB.getName());
            return result;
        }

        if (!teamB.isAlive()) {
            result.setRounds(0);
            result.setWinner(teamA.getName());
            result.addLine(teamB.getName() + " is already defeated.");
            result.addLine("Winner: " + teamA.getName());
            return result;
        }

        int rounds = 0;
        result.addLine("Raid starts: " + teamA.getName() + " vs " + teamB.getName());

        while (teamA.isAlive() && teamB.isAlive() && rounds < MAX_ROUNDS) {
            rounds++;
            result.addLine("Round " + rounds + ":");

            CombatNode targetForA = selectTarget(teamB, teamASkill);
            if (targetForA != null) {
                int hpBefore = teamB.getHealth();
                result.addLine("  " + teamA.getName() + " uses " + teamASkill.getSkillName()
                        + " [" + teamASkill.getEffectName() + "] on " + targetForA.getName());
                teamASkill.cast(targetForA);
                int dealt = Math.max(0, hpBefore - teamB.getHealth());
                result.addLine("  " + teamB.getName() + " HP: " + teamB.getHealth() + " (-" + dealt + ")");
            }

            if (!teamB.isAlive()) {
                break;
            }

            CombatNode targetForB = selectTarget(teamA, teamBSkill);
            if (targetForB != null) {
                int hpBefore = teamA.getHealth();
                result.addLine("  " + teamB.getName() + " uses " + teamBSkill.getSkillName()
                        + " [" + teamBSkill.getEffectName() + "] on " + targetForB.getName());
                teamBSkill.cast(targetForB);
                int dealt = Math.max(0, hpBefore - teamA.getHealth());
                result.addLine("  " + teamA.getName() + " HP: " + teamA.getHealth() + " (-" + dealt + ")");
            }
        }

        result.setRounds(rounds);

        if (teamA.isAlive() && !teamB.isAlive()) {
            result.setWinner(teamA.getName());
        } else if (teamB.isAlive() && !teamA.isAlive()) {
            result.setWinner(teamB.getName());
        } else {
            result.setWinner("Draw");
            result.addLine("Max rounds reached.");
        }

        result.addLine("Winner: " + result.getWinner());
        return result;
    }

    private CombatNode selectTarget(CombatNode team, Skill skill) {
        if (team == null || !team.isAlive()) {
            return null;
        }

        if (skill instanceof AreaSkill) {
            return team;
        }

        List<CombatNode> aliveLeaves = collectAliveLeaves(team);
        if (aliveLeaves.isEmpty()) {
            return null;
        }

        return aliveLeaves.get(random.nextInt(aliveLeaves.size()));
    }

    private List<CombatNode> collectAliveLeaves(CombatNode node) {
        List<CombatNode> result = new ArrayList<>();
        collectAliveLeaves(node, result);
        return result;
    }

    private void collectAliveLeaves(CombatNode node, List<CombatNode> result) {
        if (node == null || !node.isAlive()) {
            return;
        }

        if (node.getChildren().isEmpty()) {
            result.add(node);
            return;
        }

        for (CombatNode child : node.getChildren()) {
            collectAliveLeaves(child, result);
        }
    }
}