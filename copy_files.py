import shutil
import os

os.chdir('e:\\ITFac_Batch21_Group53')

files_to_replace = [
    ('src\\test\\java\\com\\example\\ui\\pages\\sales\\SalesListPageFinal.java', 'src\\test\\java\\com\\example\\ui\\pages\\sales\\SalesListPage.java'),
    ('src\\test\\java\\com\\example\\ui\\pages\\sales\\SellPlantPageFinal2.java', 'src\\test\\java\\com\\example\\ui\\pages\\sales\\SellPlantPage.java'),
    ('src\\test\\java\\com\\example\\ui\\stepDefinitions\\sales\\SellPlantStepsImpl.java', 'src\\test\\java\\com\\example\\ui\\stepDefinitions\\sales\\SellPlantSteps.java'),
    ('src\\test\\java\\com\\example\\ui\\stepDefinitions\\sales\\SalesListStepsImpl.java', 'src\\test\\java\\com\\example\\ui\\stepDefinitions\\sales\\SalesListSteps.java'),
]

for src, dst in files_to_replace:
    try:
        if os.path.exists(src):
            shutil.copy2(src, dst)
            print(f"Copied {src} to {dst}")
        else:
            print(f"Source file not found: {src}")
    except Exception as e:
        print(f"Error copying {src}: {e}")

print("All files copied successfully!")
