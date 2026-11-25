package frames;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.JComponent;

public class MainComponent extends JComponent {

    private BufferedImage backgroundImage;
    private BufferedImage llamaImage;
    private BufferedImage rainbowImage;
    private BufferedImage chickenSheet;
    private BufferedImage chickenBlack;
    private BufferedImage chickenDark;
    private BufferedImage chickenLight;
    
    private int llamaX = 100;
    private int llamaY = 0;
    private int llamaDy = 0;
    private boolean onGround = true;
    private static final int GRAVITY = 1;
    private static final int JUMP_STRENGTH = -20;
    private static final int LLAMA_SPEED = 6;
    private static final int LLAMA_W = 48;
    private static final int LLAMA_H = 48;
    
    private boolean leftPressed = false;
    private boolean rightPressed = false;
    private boolean facingLeft = false;
    private int frameIndex = 0;
    private int frameCounter = 0;
    private static final int FRAME_COUNT = 6;
    private static final int FRAME_DELAY = 4;
    
    private int scrollOffset = 0;

    private final java.util.List<Platform> platforms = new java.util.ArrayList<>();
    private int platformSpawnCounter = 0;

    private class Platform {
        int x, y, width, height;
        Platform(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }
    }
    
    
    public MainComponent() {
        setOpaque(true);
        setFocusable(true);

        addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent e) {
                switch (e.getKeyCode()) {
                    case java.awt.event.KeyEvent.VK_LEFT:
                        leftPressed = true;
                        facingLeft = true;
                        break;
                    case java.awt.event.KeyEvent.VK_RIGHT:
                        rightPressed = true;
                        facingLeft = false;
                        break;
                    case java.awt.event.KeyEvent.VK_SPACE:
                    case java.awt.event.KeyEvent.VK_UP:
                        if (onGround) {
                            llamaDy = JUMP_STRENGTH;
                            onGround = false;
                        }
                        break;
                }
            }

            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                switch (e.getKeyCode()) {
                    case java.awt.event.KeyEvent.VK_LEFT:
                        leftPressed = false;
                        break;
                    case java.awt.event.KeyEvent.VK_RIGHT:
                        rightPressed = false;
                        break;
                }
            }
        });
        
        // timer od grawitacji
            javax.swing.Timer timer = new javax.swing.Timer(16, e -> {
            if (leftPressed) {
                llamaX -= LLAMA_SPEED;
                if (llamaX < 0) llamaX = 0;
            }
            if (rightPressed) {
                llamaX += LLAMA_SPEED;
            }

            if (leftPressed || rightPressed) {
                frameCounter++;
                if (frameCounter >= FRAME_DELAY) {
                    frameCounter = 0;
                    frameIndex = (frameIndex + 1) % FRAME_COUNT;
                }
            } else {
                frameIndex = 0;
                frameCounter = 0;
            }

            llamaDy += GRAVITY;
            llamaY += llamaDy;

            onGround = false;
            for (Platform p : platforms) {
                int llamaBottom = llamaY + LLAMA_H;
                if (llamaBottom >= p.y
                        && llamaBottom <= p.y + p.height
                        && llamaX + LLAMA_W > p.x
                        && llamaX < p.x + p.width
                        && llamaDy >= 0) {
                    llamaY = p.y - LLAMA_H;
                    llamaDy = 0;
                    onGround = true;
                    break;
                }
            }

            int groundY = getHeight() - 80 - LLAMA_H;
            if (llamaY >= groundY) {
                llamaY = groundY;
                llamaDy = 0;
                onGround = true;
            }
            
            scrollOffset += 2;
            if (scrollOffset >= backgroundImage.getWidth()) {
                scrollOffset = 0;
            }
            
            java.util.Iterator<Platform> pit = platforms.iterator();
            while (pit.hasNext()) {
                Platform p = pit.next();
                p.x -= 2;
                if (p.x + p.width < 0) {
                    pit.remove();
                }
            }

            platformSpawnCounter++;
            if (platformSpawnCounter > 120) {
                platformSpawnCounter = 0;
                int spawnX = getWidth();
                int spawnY = 200 + (int) (Math.random() * 200);
                int spawnW = 100 + (int) (Math.random() * 100);
                platforms.add(new Platform(spawnX, spawnY, spawnW, 20));
            }

            repaint();
        });
        timer.start();

        try {
            backgroundImage = ImageIO.read(getClass().getResourceAsStream("/resources/background.jpg"));
            llamaImage = ImageIO.read(getClass().getResourceAsStream("/resources/llama.png"));
            rainbowImage = ImageIO.read(getClass().getResourceAsStream("/resources/rainbow.jpg"));
            chickenSheet = ImageIO.read(getClass().getResourceAsStream("/resources/Chicken_Sprite_Sheet.png"));
            chickenBlack = ImageIO.read(getClass().getResourceAsStream("/resources/Chicken_Sprite_Sheet_Black.png"));
            chickenDark = ImageIO.read(getClass().getResourceAsStream("/resources/Chicken_Sprite_Sheet_Dark_Brown.png"));
            chickenLight = ImageIO.read(getClass().getResourceAsStream("/resources/Chicken_Sprite_Sheet_Light_Brown.png"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        if (backgroundImage != null) {
            int bgW = backgroundImage.getWidth();
            int offset = scrollOffset % bgW;
            for (int x = -offset; x < getWidth(); x += bgW) {
                g2.drawImage(backgroundImage, x, 0, bgW, getHeight(), null);
            }
        }
        for (Platform p : platforms) {
            g2.setColor(new java.awt.Color(34, 139, 34)); // zielony
            g2.fillRect(p.x, p.y, p.width, p.height);
            g2.setColor(java.awt.Color.BLACK);
            g2.drawRect(p.x, p.y, p.width, p.height);
        }

        int groundY = getHeight() - 80;
        g2.setColor(new java.awt.Color(34, 139, 34));
        g2.fillRect(0, groundY, getWidth(), 80);

        // lama
        if (llamaImage != null) {
            int fy = frameIndex * LLAMA_H;
            if (fy + LLAMA_H <= llamaImage.getHeight()) {
                BufferedImage frame = llamaImage.getSubimage(0, fy, LLAMA_W, LLAMA_H);
                if (facingLeft) {
                    g2.drawImage(frame,
                            llamaX + LLAMA_W, llamaY,
                            llamaX, llamaY + LLAMA_H,
                            0, 0, LLAMA_W, LLAMA_H, null);
                } else {
                    g2.drawImage(frame, llamaX, llamaY, LLAMA_W, LLAMA_H, null);
                }
            }
        }

        if (rainbowImage != null) {
            g2.drawImage(rainbowImage, 100, 20, 24, 6, null);
        }

        if (chickenSheet != null) {
            BufferedImage frame = chickenSheet.getSubimage(0, 0, 32, 32);
            g2.drawImage(frame, 20, 100, 32, 32, null);
        }

        if (chickenBlack != null) {
            BufferedImage frame = chickenBlack.getSubimage(0, 0, 32, 32);
            g2.drawImage(frame, 100, 100, 32, 32, null);
        }

        if (chickenDark != null) {
            BufferedImage frame = chickenDark.getSubimage(0, 0, 32, 32);
            g2.drawImage(frame, 180, 100, 32, 32, null);
        }

        if (chickenLight != null) {
            BufferedImage frame = chickenLight.getSubimage(0, 0, 32, 32);
            g2.drawImage(frame, 260, 100, 32, 32, null);
        }    
    }
    
}