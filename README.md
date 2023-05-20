
<p align="center">
	<h1 align="center">slang</h1>
	<p align="center">The scholars' programming language</p>
</p>

This repository is part of a lecture series on compiler construction at the [Baden-Württemberg Cooperative State University Karlsruhe](https://www.karlsruhe.dhbw.de/en/general/about-dhbw-karlsruhe.html) in Germany. Special to this university are the career-integrated undergraduate study programs. In alternating three-month phases, students learn theory at the university and receive practical training from an enterprise. For a listing of lecture content, see section T3INF4211 in the [university's module handbook](https://www.dhbw.de/fileadmin/user/public/SP/KA/Informatik/Informatik.pdf).

**Caution: Contrary to general conventions, various commits may contain incomplete states, causing several tests to break. These commits are preceded by an exclamation mark in the commit message. The background to this is that the students are to implement these very functions in a test-driven manner.**

## Setup guide

1. Open the terminal and navigate to the folder where all your other Java projects are located.
2. Clone the repository using the command `git clone https://github.com/university-lectures/slang.git`
3. Import the `slang` folder as a project into an IDE of your choice.
4. Make sure that Apache Maven is installed either directly or embedded in your IDE.
5. Run `mvn test` in the terminal or a similar sounding function of your IDE.

## Contribute to this project

The project is kept small on purpose. It does not intend to offer as many features as possible or to be particularly powerful. Please consider this before creating a pull request. Functional enhancements are likely to be rejected. Changes, however, that serve usability, clarity, structure or better understanding are always welcome.

### Branch Naming Convention

Temporary branch names should consist of three parts separated by `/`.

1. The first part is used for categorization and can take one of the following identifiers:
   * `feature` for branches that add, refactor or remove a specific feature
   * `bugfix` for branches that fix a bug
   * `experiment` for branches that are used to collaborate on experiments
2. The second part references the issue/ticket you are currently working on. If there's no reference, just add `no-ref`.
3. The third part describes the purpose of this particular branch. This description should as short and meaningful as possible.

Please separate words with a dash `-` (known as "kebap case") throughout the branch name. Here are a few examples:

```zsh
git branch feature/issue-5/parse-additive-expressions
git branch bugfix/no-ref/fix-selection-set-for-exponent
```

© Marco Haupt 2023
