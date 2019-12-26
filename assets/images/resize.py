import os
import glob
from PIL import Image

image_folder = os.getcwd()+"\\"

# demo how to use glob for finding files
#print glob.glob(image_folder+"crops\\*.png")
#print glob.glob(image_folder+"forageables\\*.png")
#print glob.glob(image_folder+"ores\\*.png")
#print glob.glob(image_folder+"ores\\*Item.png")

image_paths_to_resize = glob.glob(image_folder+"crops\\*Item.png")
for path in image_paths_to_resize:
    img = Image.open(path)
    print path
    print (img.width/4, img.height/4)
    img.resize((img.width/4, img.height/4), Image.NEAREST) \
       .save(path[:-4]+".Small.png")
