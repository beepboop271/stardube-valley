import os
import glob
from PIL import Image

image_folder = os.getcwd()+"\\assets\\images\\"

# demo how to use glob for finding files
print glob.glob(image_folder+"crops\\*.png")
print glob.glob(image_folder+"forageables\\*.png")
print glob.glob(image_folder+"ores\\*.png")
print glob.glob(image_folder+"ores\\*Item.png")

image_paths_to_resize = []
for path in image_paths_to_resize:
    img = Image.open(path)
    img.resize((img.width*4, img.height*4), Image.NEAREST) \
       .save(path[:-3]+"resized.png")
