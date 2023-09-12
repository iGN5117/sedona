import io

sedonaMap = dict()
sedona_missing_lines = ["| # | Sedona | Engine Name |\n", "| --- | --- | --- |\n"]

def get_file_lines(file_path):
    try:
        with io.open(file_path, 'r', encoding='utf-8') as file:
            lines = file.readlines()
            return lines
    except FileNotFoundError:
        print(f"File not found: {file_path}")
    except IOError as e:
        print(f"Error reading the file: {e}")


def process_engine_lines(engine_file_path, engine_name):
    try:
        with io.open(engine_file_path, 'r', encoding='utf-8') as file:
            for line in file:
                strippedLine = line.strip()
                if '#' in strippedLine or len(strippedLine) == 0 or 'rs_' in strippedLine.lower() or 'rst_' in strippedLine.lower(): continue
                funcName = strippedLine.lower().replace('st_', '')
                funcNameAlternative = funcName.replace('geometry', 'geom')
                if funcName in sedonaMap or funcNameAlternative in sedonaMap:
                    if funcName in sedonaMap:
                        line_index = sedonaMap[funcName]
                    else:
                        line_index = sedonaMap[funcNameAlternative]
                    sedona_line = sedona_data_lines[line_index]
                    engine_index = get_engine_index(engine_name)
                    sedona_data_lines[line_index] = edit_line(sedona_line, engine_index)
                else:
                    addMissingFunc(strippedLine, get_full_engine_name(engine_name))


    except FileNotFoundError:
        print(f"File not found: {engine_file_path}")
    except IOError as e:
        print(f"Error reading the file: {e}")

def get_full_engine_name(engine_name):
    if engine_name == 'redshift':
        return 'RedShift'
    elif engine_name == 'mosaic':
        return 'Databricks Mosaic'
    elif engine_name == 'bigquery':
        return 'BigQuery'
    elif engine_name == 'snowflake':
        return 'Snowflake'


def addMissingFunc(funcName, engine_name):
    line_num = len(sedona_missing_lines) - 1
    sedona_missing_lines.append("| {0} | {1} | {2} |\n".format(line_num, funcName, engine_name))

def create_map_for_sedona(lines: [str]):
    for i in range(len(lines)):
        currLine = lines[i].strip()
        currLineParts = currLine.split('|')
        funcNum = currLineParts[1].strip()
        funcName = currLineParts[2].strip()
        if not funcNum.isdigit():
            continue

        sedonaMap[funcName.lower().replace('st_', '')] = i


def get_engine_index(engine_name):
    engine_name = engine_name.lower()
    if 'mosaic' in engine_name:
        return 3
    elif engine_name == 'snowflake':
        return 4
    elif engine_name == 'bigquery':
        return 5
    elif engine_name == 'redshift':
        return 6


def edit_line(line, engine_num):
    parts = line.strip().split('|')
    function_number = parts[1]
    function_name = parts[2]
    parts[engine_num] = "\u2705"
    updated_line = '|'.join(parts)
    updated_line += '\n'

    # Ensure the engine_values list has exactly 4 elements
    # if len(engine_values) != 4:
    #     raise ValueError("engine_values should contain exactly 4 elements")
    #
    # # Update the engine values in the string
    # updated_line = f"| {function_number} | {function_name} | {' | '.join(engine_values)} |"

    return updated_line

def writeToFile(file_path, lines):
    with io.open(file_path, 'w', encoding='utf-8') as file:
        file.writelines(lines)



engine_files = {'snowflake': '/Users/nileshgajwani/Desktop/spatial-function-lists/snowflake',
                'redshift': '/Users/nileshgajwani/Desktop/spatial-function-lists/redshift',
                'mosaic': '/Users/nileshgajwani/Desktop/spatial-function-lists/mosaic',
                'bigquery': '/Users/nileshgajwani/Desktop/spatial-function-lists/bigquery'}

sedona_file_path = '/Users/nileshgajwani/Desktop/spatial-function-lists/sedona'
sedona_data_lines = get_file_lines(sedona_file_path)
sedona_output_path = '/Users/nileshgajwani/Desktop/spatial-function-lists/sedona_output'
sedona_missing_output_path = '/Users/nileshgajwani/Desktop/spatial-function-lists/sedona_missing_output'
create_map_for_sedona(sedona_data_lines)
for engine in engine_files.keys():
    process_engine_lines(engine_file_path=engine_files[engine], engine_name=engine)

#writeToFile(sedona_output_path, sedona_data_lines)
writeToFile(sedona_missing_output_path, sedona_missing_lines)
print("done")
