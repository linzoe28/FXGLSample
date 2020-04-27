/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.fxglsample;

import com.almasb.fxgl.app.FXGL;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.entity.Entities;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.settings.GameSettings;
import com.sun.media.jfxmedia.events.PlayerEvent;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author user
 */
public class NewFXMain extends GameApplication {

    private Entity player = null;
    private Entity block = null;
    private Color[] colors = new Color[]{Color.AQUAMARINE, Color.DARKMAGENTA, Color.LIGHTSALMON, Color.PALEVIOLETRED};

    enum Type {

        PLAYER, BLOCK
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    protected void initSettings(GameSettings gs) {
        gs.setWidth(800);
        gs.setHeight(600);
        gs.setTitle("Hello World~");
    }

    @Override
    protected void onUpdate(double tpf) {
        super.onUpdate(tpf); //To change body of generated methods, choose Tools | Templates.
//        if(player.getX()>this.getInput().getMouseXWorld()){
//            player.translateX(-50*tpf);
//            player.setRotation(180);
//        }else if(player.getX()<this.getInput().getMouseXWorld()){
//            player.translateX(50*tpf);
//        }
//        if(player.getY()>this.getInput().getMouseYWorld()){
//            player.translateY(-50*tpf);
//            player.setRotation(270);
//        }else if(player.getY()<this.getInput().getMouseYWorld()){
//            player.translateY(50*tpf);
//            player.setRotation(90);
//        }
    }

    @Override
    protected void initPhysics() {
        super.initPhysics(); //To change body of generated methods, choose Tools | Templates.
        FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(Type.PLAYER, Type.BLOCK) {

            @Override
            protected void onCollision(Entity a, Entity b) {
                b.removeFromWorld();
                HBox hbox = (HBox) a.getView().getNodes().get(0);
                Node newNode=b.getView().getNodes().get(0);
                TranslateTransition transition=new TranslateTransition(Duration.seconds(0.5));
                transition.setFromY(-5);
                transition.setToY(5);
                transition.setAutoReverse(true);
                transition.setNode(newNode);
                transition.setCycleCount(-1);
                transition.play();
                hbox.getChildren().add(0, newNode);
                a.getBoundingBoxComponent().clearHitBoxes();
                a.getBoundingBoxComponent().addHitBox(new HitBox(BoundingShape.box((hbox.getChildren().size() - 1) * 20 + 40, 10)));
            }

        });
    }

    @Override
    protected void initGame() {
        super.initGame(); //To change body of generated methods, choose Tools | Templates.
        HBox hbox = new HBox();
        Image image = new Image("https://cdn0.iconfinder.com/data/icons/brown-bear-emoticon-filled/64/cute_bear_face_avatar-23-512.png");
        ImageView playerFX = new ImageView(image);
        //Circle playerFX=new Circle(10, Color.AQUA);
        playerFX.setFitHeight(30);
        playerFX.setPreserveRatio(true);
        playerFX.setRotate(90);
        hbox.getChildren().add(playerFX);
        player = Entities.builder().at(200, 200).viewFromNode(hbox).
                bbox(new HitBox(BoundingShape.circle(10))).with(new CollidableComponent(true)).type(Type.PLAYER).buildAndAttach(this.getGameWorld());

        for (int i = 0; i < 10; i++) {
            Circle blockFX = new Circle(10);
            
            blockFX.setFill(colors[(int) Math.ceil(Math.random() * colors.length) - 1]);
            Entities.builder().at(Math.random() * 800, Math.random() * 600).viewFromNode(blockFX).
                    bbox(new HitBox(BoundingShape.circle(10))).with(new CollidableComponent(true)).type(Type.BLOCK).buildAndAttach(getGameWorld());
        }
    }

    @Override
    protected void initInput() {
        super.initInput(); //To change body of generated methods, choose Tools | Templates.
        Input input = this.getInput();
        input.addAction(new UserAction("up") {
            @Override
            protected void onAction() {
                player.setRotation(270);
                player.getPositionComponent().translateY(-1);
            }
        }, KeyCode.UP);

        input.addAction(new UserAction("down") {
            @Override
            protected void onAction() {
                player.setRotation(90);
                player.getPositionComponent().translateY(1);
            }
        }, KeyCode.DOWN);

        input.addAction(new UserAction("left") {
            @Override
            protected void onAction() {
                player.setRotation(180);
                player.getPositionComponent().translateX(-1);
            }
        }, KeyCode.LEFT);

        input.addAction(new UserAction("right") {
            @Override
            protected void onAction() {
                player.setRotation(0);
                player.getPositionComponent().translateX(1);
            }
        }, KeyCode.RIGHT);
    }

}
