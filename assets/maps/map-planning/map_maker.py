import pygame

pygame.init()

COLOURS = {
    (211, 163, 71 ): ".",
    (21 , 255, 0  ): "x",
    (0  , 255, 203): "o"
}

img = pygame.image.load("mineArea.png")

with open("Ocean", "w") as f:
    for y in range(img.get_height()):
        line = []
        for x in range(img.get_width()):
            clr = img.get_at((x, y))
            if clr.a > 0:
                line.append(COLOURS.get((clr.r, clr.g, clr.b)))
            else:
                line.append(" ")
        f.write("".join(line)+"\n")