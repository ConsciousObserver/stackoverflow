package com.example;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

import com.example.TestMaze.Cell.VisitStatus;

public class TestMaze {
	static List<Cell> resultPath = null;
	
	public static void main(String[] args) {
		String filePath = "maze2.txt";
		Maze currentMaze = new Maze(filePath);

		findPath(currentMaze, currentMaze.startCell);
		
		if(resultPath == null) {
			System.out.println("\nNo path exists for the Maze");
		} else {
			System.out.println("\nPath size : " + resultPath.size());
			printPathOnMaze(currentMaze, resultPath);
		}
	}
	
	private static void printPathOnMaze(Maze currentMaze, List<Cell> path) {
		path.stream()
			.forEach(cell-> cell.setCh('O'));
		
		currentMaze.printCells();
	}

	private static List<Cell> findPath(Maze currentMaze, Cell current) {
		if(currentMaze.isEndCell(current)) {
			resultPath = new ArrayList<>();
			Cell traversalCell = current;
			
			while(traversalCell != null) {
				resultPath.add(0, traversalCell);
				traversalCell = traversalCell.getParentCell();
			}
			return resultPath;
		}
		
		if(resultPath == null) {
		
			if(Maze.isWall(current)) {
				current.setVisitStatus(VisitStatus.VISITED);
			} else {
				current.setVisitStatus(VisitStatus.IN_PROGRESS);
				List<Cell> neighbourList = currentMaze.getNeighbours(current);
				
				neighbourList.stream()
					.filter(cell -> cell.getVisitStatus() == VisitStatus.UNVISITED)
					.filter(cell -> cell.getVisitStatus() == VisitStatus.UNVISITED)
					.forEach(neighbour -> {
						neighbour.setParentCell(current);
						findPath(currentMaze, neighbour);	
					});
				
				current.setVisitStatus(VisitStatus.VISITED);
			}
		}
		
		return null;
	}
	
	public static boolean isCellInPath(Cell cell, List<Cell> path) {
		return path.stream().anyMatch(c -> c.getI() == cell.getI() && c.getJ() == c.getJ());
	}
	
	public static class Cell {
		private int i, j;
		private char ch;
		
		private Cell parentCell;
		
		public enum VisitStatus {VISITED, IN_PROGRESS, UNVISITED};
		
		private VisitStatus visitStatus = VisitStatus.UNVISITED;
		
		public Cell(int i, int j, char ch) {
			super();
			this.i = i;
			this.j = j;
			this.ch = ch;
		}

		public int getI() {
			return i;
		}

		public int getJ() {
			return j;
		}

		public char getCh() {
			return ch;
		}
		
		public void setCh(char ch) {
			this.ch = ch;
		}

		public VisitStatus getVisitStatus() {
			return visitStatus;
		}

		public void setVisitStatus(VisitStatus visitStatus) {
			this.visitStatus = visitStatus;
		}

		public Cell getParentCell() {
			return parentCell;
		}

		public void setParentCell(Cell parentCell) {
			this.parentCell = parentCell;
		}
	}
	
	public static class Maze {
		private Cell[][] grid;
		private Cell startCell;
		private Cell endCell;
		
		private static final char START_CELL_CHAR = 'S';
		private static final char END_CELL_CHAR = 'E';
		private static final char WALL_CHAR = '#';
		private static final char EMPTY_SPACE_CHAR = '.';
		
		public Maze(String filePath) {
			grid = createFromFile(filePath);
			printCells();
		}

		public Cell[][] getGrid() {
			return grid;
		}
		
		public Cell getStartCell() {
			return startCell;
		}

		public Cell getEndCell() {
			return endCell;
		}
		
		public boolean isEndCell(Cell cell) {
			return endCell.getI() == cell.getI() && endCell.getJ() == cell.getJ();
		}
		
		List<Cell> getNeighbours(Cell cell) {
			List<Cell> neighboursList = new ArrayList<>();
			int mazeHeight = grid.length;
			int mazeWidth = grid[0].length;
			
			if(cell.getI() - 1 > 0) {
				neighboursList.add(grid[cell.getI() - 1][cell.getJ()]);
			}
			if(cell.getI() + 1 < mazeHeight) {
				neighboursList.add(grid[cell.getI() + 1][cell.getJ()]);
			}
			if(cell.getJ() - 1 > 0) {
				neighboursList.add(grid[cell.getI()][cell.getJ() - 1]);
			}
			if(cell.getJ() + 1 < mazeWidth) {
				neighboursList.add(grid[cell.getI()][cell.getJ() + 1]);
			}
			return neighboursList;
		}
		
		public static boolean isWall(Cell cell) {
			return cell.getCh() == WALL_CHAR;
		}
		
		public static boolean isEmptySpace(Cell cell) {
			return cell.getCh() == EMPTY_SPACE_CHAR;
		}
		
		public void printCells() {
			Stream.of(grid).forEach(row-> {
				Stream.of(row).forEach(cell -> System.out.print(cell.getCh()) );
				System.out.println();
			});
			
		}

		private Cell[][] createFromFile(String filePath) {
			Cell[][] maze = null;
			try(Scanner scan = new Scanner(Paths.get(filePath)) ) {
				List<Cell[]> list = new ArrayList<>();
				
				for(int i = 0; scan.hasNext(); i++) {
					String line = scan.nextLine();
					char[] chArr = line.toCharArray();
					Cell[] row = new Cell[chArr.length];
					
					for(int j = 0; j < chArr.length; j++) {
						char ch = chArr[j];
						Cell cell = new Cell(i, j, ch);
						row[j] = cell;
						if(ch == START_CELL_CHAR) {
							startCell = cell;
						} else if (ch == END_CELL_CHAR) {
							endCell = cell;
						}
					}
					
					list.add(row);
				}
				
				if(startCell == null || endCell == null) {
					throw new RuntimeException("Start cell or End cell not present");
				}
				maze = list.toArray(new Cell[][]{});
			} catch(Exception ex) {
				ex.printStackTrace();
			}
			
			
			return maze;
		}
	}
}