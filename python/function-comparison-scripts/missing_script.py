import io


output_lines = []

sedona_file_path = '/Users/nileshgajwani/Desktop/spatial-function-lists/sedona'
sedona_data_lines = get_file_lines(sedona_file_path)
sedona_output_path = '/Users/nileshgajwani/Desktop/spatial-function-lists/sedona_output'
create_map_for_sedona(sedona_data_lines)
for engine in engine_files.keys():
    process_engine_lines(engine_file_path=engine_files[engine], engine_name=engine)