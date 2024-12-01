package com.projectoop.game.sprites.trap;

import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.projectoop.game.GameWorld;
import com.projectoop.game.scences.Hud;
import com.projectoop.game.screens.PlayScreen;
import com.projectoop.game.sprites.Knight;
import com.projectoop.game.sprites.items.ItemDef;
import com.projectoop.game.sprites.items.Potion;
import com.projectoop.game.tools.AudioManager;

public class Coin extends InteractiveTileObject{
    private static TiledMapTileSet tileSet;
    // the ID of the new visualization we need to use to replace the coin after collision
    private final int BLANK_COIN = 28;

//    public Coin(PlayScreen screen, MapObject object) {
//        super(screen, object);
//        tileSet = map.getTileSets().getTileSet("finished_map");
//        fixture.setUserData(this);
//        setCategoryFilter(GameWorld.COIN_BIT);
//    }
    public Coin(PlayScreen screen, MapObject object) {
        super(screen, object);
        tileSet = map.getTileSets().getTileSet("finished_map");

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(8/GameWorld.PPM, 8/GameWorld.PPM);

        fdef.shape = shape;
        fdef.filter.categoryBits = GameWorld.COIN_BIT;
        fdef.filter.maskBits = (short) GameWorld.KNIGHT_BIT;
//        fdef.isSensor = true; // Giữ nguyên chế độ sensor

        fixture = body.createFixture(fdef);
        fixture.setUserData(this);

        setCategoryFilter(GameWorld.COIN_BIT);
    }

    @Override
    public void onHeadHit(Knight knight) {
        Gdx.app.log("Coin", "Knight hit coin");
        if(getCell().getTile().getId() == BLANK_COIN)
            AudioManager.manager.get("audio/sounds/bump.wav", Sound.class).play();
        else {
            if (object.getProperties().containsKey("mushroom")) {
                screen.spawnItem(new ItemDef(new Vector2(body.getPosition().x, body.getPosition().y + 16 / GameWorld.PPM), Potion.class));
                AudioManager.manager.get("audio/sounds/powerup_spawn.wav", Sound.class).play();
            } else
                AudioManager.manager.get("audio/sounds/coin.wav", Sound.class).play();
            getCell().setTile(tileSet.getTile(BLANK_COIN));
            Hud.addScore(300);
        }
    }

    @Override
    public void onFootHit(Knight knight) {
        // empty
    }
    @Override
    public void passThisRound(Knight knight) {
        // empty
    }
}
